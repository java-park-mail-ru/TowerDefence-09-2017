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

import java.util.*;

@Service
public class GameManager {

    @NotNull
    private final Logger log = LoggerFactory.getLogger(GameManager.class);


    private static final int GAME_LOBBY_SIZE = 2;

    @NotNull
    private final GameSessionService gameSessionService;

    @NotNull
    private final ServerSnaphotService serverSnaphotService;

    @NotNull
    private final UserDao userDao;

    @NotNull
    private final MonsterWaveProcessorService waveProcessor;

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
                       @NotNull TransportService transportService,
                       @NotNull TowerShootingService towerShootingService) {
        this.gameSessionService = gameSessionService;
        this.serverSnaphotService = serverSnaphotService;
        this.userDao = userDao;
        this.waveProcessor = waveProcessor;
        this.towerManager = towerManager;
        this.towerShootingService = towerShootingService;
        this.transportService = transportService;
    }

    public void tryStartGameSession(GameContext context) {
        long stamp = context.lockQueue();
        try {
            Queue<Long> waiters = context.getWaiters();
            if (waiters.size() < GAME_LOBBY_SIZE) {
                return;
            }

            Set<User> matched = new HashSet<>();
            while (waiters.size() > 0 && matched.size() < GAME_LOBBY_SIZE) {
                Long userId = waiters.poll();

                User user = userDao.getUserById(userId);
                if (transportService.isConnected(user.getId())) {
                    matched.add(user);
                }
            }

            if (matched.size() == GAME_LOBBY_SIZE && gameSessionService.startGame(matched, context)) {
                log.trace("GameSession started in thread {}", Thread.currentThread().getId());
            } else {
                log.warn("Fail on start game session, thread {}", Thread.currentThread().getId());
                matched.forEach(user -> context.getWaiters().add(user.getId()));
            }

        } catch (Exception e) {
            log.error("Exception on game start : {}", e);
            context.clearWaitersQueue();
        } finally {
            context.unlockQueue(stamp);
        }
    }

    public void gameStep(long time, GameContext context) {
        tryStartGameSession(context);
        Set<GameSession> sessions = context.getSessions();
        List<GameSession> invalidSessions = new ArrayList<>();
        List<GameSession> finishedSessions = new ArrayList<>();

        long delta = context.updateTimeBuffer(time);

        Queue<TowerManager.TowerOrder> orders = context.getTowerOrders();
        TowerManager.TowerOrder order;
        while ((order = orders.poll()) != null) {
            log.debug("Order for player {} in process", order.getPlayerId());
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

        invalidSessions.forEach(session -> gameSessionService.terminateSession(session, CloseStatus.SERVER_ERROR, context));
        finishedSessions.forEach(session -> gameSessionService.finishGame(session, context));
    }

}
