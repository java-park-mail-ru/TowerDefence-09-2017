package com.td.game.services;

import com.td.daos.UserDao;
import com.td.game.GameSession;
import com.td.game.domain.*;
import com.td.game.resource.ResourceFactory;
import com.td.game.snapshots.GameInitMessage;
import com.td.websocket.TransportService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameInitService {

    @NotNull
    private final TransportService transport;
    @NotNull
    private final ResourceFactory resourceFactory;

    @NotNull
    private final TextureAtlas textureAtlas;

    public GameInitService(@NotNull TransportService transportService,
                           @NotNull UserDao userDao,
                           @NotNull ResourceFactory resourceFactory) {
        this.transport = transportService;
        this.resourceFactory = resourceFactory;
        textureAtlas = resourceFactory.loadResource("TextureAtlas.json", TextureAtlas.class);
    }

    public void initGameInSession(@NotNull GameSession session) {
        List<Player.PlayerSnapshot> playerSnapshots = session.getPlayers()
                .stream()
                .map(Player::getSnapshot)
                .collect(Collectors.toList());

        for (Player player : session.getPlayers()) {
            GameInitMessage message = createInitMessage(session, player, playerSnapshots);
            try {
                transport.sendMessageToUser(player.getId(), message);
            } catch (IOException e) {
                throw new SnapshotSendingException("unable to send initial message", e);
            }
        }
    }

    @NotNull
    private GameInitMessage createInitMessage(@NotNull GameSession session,
                                              Player player,
                                              List<Player.PlayerSnapshot> playerSnapshots) {
        Wave.WaveSnapshot current = session.getCurrentWave().getSnapshot();
        GameMap.GameMapSnapshot map = session.getGameMap().getSnapshot();
        PlayerClass playerClass = session.getPlayersClasses().get(player.getId());
        List<Integer> availableTowers = playerClass.getAvailableTowers();
        return new GameInitMessage(
                player.getId(),
                map,
                playerSnapshots,
                availableTowers,
                current,
                textureAtlas,
                session.getHp());

    }
}
