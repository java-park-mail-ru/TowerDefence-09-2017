package com.td.game;

import com.td.daos.UserDao;
import com.td.domain.User;
import com.td.game.services.*;
import com.td.websocket.TransportService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Service
public class GameManager {

    @NotNull
    private final Logger log = LoggerFactory.getLogger(GameManager.class);

    //temoral hack for development
    private static final int GAME_LOBBY_SIZE = 1;

    @NotNull
    private final GameSessionService gameSessionService;

    @NotNull
    private final ServerSnaphotService serverSnaphotService;

    @NotNull
    private final ConcurrentLinkedQueue<Long> waiters = new ConcurrentLinkedQueue<>();

    @NotNull
    private final ConcurrentLinkedQueue<TowerManager.TowerOrder> towerOrders = new ConcurrentLinkedQueue<>();

    @NotNull
    private final UserDao userDao;

    @NotNull
    private final MonsterWaveProcessorService waveProcessor;

    private long timeBuffer = 0L;

    @NotNull
    private final TowerManager towerManager;

    @NotNull
    private final TowerShootingService towerShootingService;

    @NotNull
    private final TransportService transportService;


    public GameManager(@NotNull GameSessionService gameSessionService,
                       @NotNull ServerSnaphotService serverSnaphotService,
                       @NotNull UserDao userDao,
                       @NotNull MonsterWaveProcessorService waveProcessor,

                       @NotNull TowerManager towerManager,
                       @NotNull TowerShootingService towerShootingService,
                       @NotNull TransportService transportService) {
        this.gameSessionService = gameSessionService;
        this.serverSnaphotService = serverSnaphotService;
        this.userDao = userDao;
        this.waveProcessor = waveProcessor;
        this.towerManager = towerManager;
        this.towerShootingService = towerShootingService;
        this.transportService = transportService;
    }

    public void addUser(Long id) {
        if (gameSessionService.isPlaying(id)) {
            return;
        }
        waiters.offer(id);
    }

    public void tryStartGameSession() {
        List<User> matched = new ArrayList<>();

        if (waiters.size() >= GAME_LOBBY_SIZE) {
            for (int i = 0; i < GAME_LOBBY_SIZE; ++i) {
                Long userId = waiters.poll();
                User user = userDao.getUserById(userId);
                if (transportService.isConnected(userId)) {
                    matched.add(user);
                }
            }
        }
        matched = matched.stream().distinct().collect(Collectors.toList());

        if (matched.size() == GAME_LOBBY_SIZE) {
            matched.forEach(user -> log.trace("User {} in game", user.getId()));
            gameSessionService.startGame(matched);
        } else {
            matched.forEach(user -> waiters.add(user.getId()));
        }
    }

    public void gameStep(long time) {
        tryStartGameSession();
        Set<GameSession> sessions = gameSessionService.getSessions();
        List<GameSession> invalidSessions = new ArrayList<>();
        List<GameSession> finishedSessions = new ArrayList<>();
        long delta = time - timeBuffer;
        timeBuffer = time;
        TowerManager.TowerOrder order;
        while ((order = towerOrders.poll()) != null) {
            log.trace("Order for player {} in process", order.getPlayerId());
            GameSession session = gameSessionService.getSessionForUser(order.getPlayerId());
            towerManager.processOrder(session, order);
        }

        for (GameSession session : sessions) {
            if (!gameSessionService.isValidSession(session)) {
                log.warn("Session is invalid, id: ", session.getId());
                invalidSessions.add(session);
                continue;
            }

            waveProcessor.processPassedMonsters(session);
            towerShootingService.reloadTowers(session, delta);
            towerShootingService.processTowerShooting(session);
            waveProcessor.processCleanup(session);
            waveProcessor.processWave(session, delta);

            if (session.isFinished()) {
                log.trace("Session is finished, id: ", session.getId());
                finishedSessions.add(session);
            } else {
                serverSnaphotService.sendServerSnapshot(session);
            }
        }

        invalidSessions.forEach(session -> gameSessionService.terminateSession(session, CloseStatus.SERVER_ERROR));
        finishedSessions.forEach(gameSessionService::finishGame);
    }

    public void addTowerOrders(Integer orderedTowerTypeId, long xcoord, long ycoord, Long id) {
        towerOrders.offer(new TowerManager.TowerOrder(xcoord, ycoord, orderedTowerTypeId, id));
    }

}
