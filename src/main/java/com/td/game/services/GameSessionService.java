package com.td.game.services;

import com.td.daos.ScoresDao;
import com.td.daos.UserDao;
import com.td.domain.User;
import com.td.game.GameContext;
import com.td.game.GameSession;
import com.td.game.domain.Player;
import com.td.game.snapshots.GameFinishMessage;
import com.td.websocket.TransportService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameSessionService {
    @NotNull
    private final Map<Long, GameSession> usersSessions = new HashMap<>();

    @NotNull
    private final TransportService transport;

    @NotNull
    private final UserDao userDao;

    @NotNull
    private final ScoresDao scoresDao;

    @NotNull
    private final GameInitService gameInitService;

    @NotNull
    private final GameContextService gameContextService;

    public GameSessionService(@NotNull TransportService transport,
                              @NotNull UserDao userDao,
                              @NotNull ScoresDao scoresDao,
                              @NotNull GameInitService gameInitService,
                              @NotNull GameContextService gameContextService) {
        this.transport = transport;

        this.userDao = userDao;
        this.scoresDao = scoresDao;
        this.gameInitService = gameInitService;
        this.gameContextService = gameContextService;
    }


    public void startGame(List<User> users, GameContext context) {

        GameSession session = gameInitService.createGameSession(users);
        gameInitService.initGameInSession(session, context);
        context.addSession(session);
        users.forEach(user -> {
            gameContextService.registerUserMapping(user.getId(), context);
            usersSessions.put(user.getId(), session);
        });
    }

    public boolean isPlaying(Long id) {
        return usersSessions.containsKey(id);
    }

    public boolean isValidSession(@NotNull GameSession session) {
        return session
                .getPlayers()
                .stream()
                .allMatch(player -> transport.isConnected(player.getId()));
    }

    public void terminateSession(@NotNull GameSession session, CloseStatus status, GameContext context) {
        List<Player> players = session.getPlayers();
        for (Player player : players) {
            usersSessions.remove(player.getId());
            gameContextService.unregisterUserMapping(player.getId());
            transport.closeSession(player.getId(), status);
        }
        context.removeSession(session);
    }


    public void finishGame(@NotNull GameSession session, GameContext context) {
        for (Player player : session.getPlayers()) {
            User user = userDao.getUserById(player.getId());
            scoresDao.addScores(user, player.getScores());
            usersSessions.remove(user.getId());
            gameContextService.unregisterUserMapping(player.getId());
            try {
                transport.sendMessageToUser(player.getId(), new GameFinishMessage(player.getScores()));
                transport.closeSession(player.getId(), CloseStatus.NORMAL);
            } catch (IOException e) {
                transport.closeSession(player.getId(), CloseStatus.SERVER_ERROR);
            }
        }
        context.removeSession(session);
    }

    public GameSession getSessionForUser(Long playerId) {
        return usersSessions.get(playerId);
    }

}
