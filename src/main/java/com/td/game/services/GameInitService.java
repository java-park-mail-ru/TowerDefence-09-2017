package com.td.game.services;

import com.td.daos.UserDao;
import com.td.game.GameSession;
import com.td.game.domain.GameMap;
import com.td.game.domain.Player;
import com.td.game.domain.PlayerClass;
import com.td.game.domain.Wave;
import com.td.game.gameObjects.Tower;
import com.td.game.resourceSystem.ResourceFactory;
import com.td.game.snapshots.GameInitMessage;
import com.td.websocket.TransportService;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class GameInitService {

    @NotNull
    private final TransportService transport;

    @NotNull
    private final UserDao userDao;


    public GameInitService(@NotNull TransportService transportService, @NotNull UserDao userDao, @NotNull ResourceFactory resourceFactory) {
        this.transport = transportService;
        this.userDao = userDao;
    }

    public void initGameInSession(@NotNull GameSession session) {
        List<Player.PlayerSnapshot> playerSnapshots = session.getPlayers()
                .stream()
                .map(Player::getSnapshot)
                .collect(Collectors.toList());

        for (Player player : session.getPlayers()) {
            GameInitMessage message = createIntiMessage(session, player, playerSnapshots);
            try {
                transport.sendMessageToUser(player.getId(), message);
            } catch (IOException e) {
                //TODO: terminate sessions
                e.printStackTrace();
            }
        }
    }

    private GameInitMessage createIntiMessage(@NotNull GameSession session,
                                              Player player,
                                              List<Player.PlayerSnapshot> playerSnapshots) {
        Wave.WaveSnapshot current = session.getCurrentWave().getSnapshot();
        GameMap.GameMapSnapshot map = session.getGameMap().getSnapshot();
        PlayerClass playerClass = session.getPlayersClasses().get(player.getId());
        List<Tower.TowerSnapshot> availableTowers = playerClass
                .getAvailableTowers().stream()
                .map(Tower::getSnapshot)
                .collect(Collectors.toList());
        return new GameInitMessage(map, playerSnapshots, availableTowers, current);


    }


}
