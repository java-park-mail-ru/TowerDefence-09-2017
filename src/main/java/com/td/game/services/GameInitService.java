package com.td.game.services;

import com.td.domain.User;
import com.td.game.GameSession;
import com.td.game.domain.*;
import com.td.game.gameobjects.Path;
import com.td.game.resource.ResourceFactory;
import com.td.game.snapshots.GameInitMessage;
import com.td.websocket.TransportService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GameInitService {

    @NotNull
    private final TransportService transport;
    @NotNull
    private final ResourceFactory resourceFactory;

    @NotNull
    private final PathGenerateService pathGenerator;

    private TextureAtlas textureAtlas;

    @NotNull
    private final MonsterWaveGenerator waveService;
    private List<GameParams> gameSessionsParams;


    public GameInitService(@NotNull TransportService transportService,
                           @NotNull ResourceFactory resourceFactory,
                           @NotNull PathGenerateService pathGenerator,
                           @NotNull MonsterWaveGenerator waveService) {
        this.transport = transportService;
        this.resourceFactory = resourceFactory;
        this.pathGenerator = pathGenerator;
        this.waveService = waveService;
    }

    @PostConstruct
    public void init() {
        this.textureAtlas = resourceFactory.loadResource("TextureAtlas.json", TextureAtlas.class);
        this.gameSessionsParams = resourceFactory.loadResourceList("gameParams/GameParamsList.json", GameParams.class);


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

    @NotNull GameSession createGameSession(Set<User> users) {
        GameMap map = new GameMap(resourceFactory.loadResource("GameMap.json", GameMap.GameMapResource.class));

        Path path = pathGenerator.generatePath();
        List<Path> paths = new ArrayList<>();
        paths.add(path);
        map.setPathTiles(path.getPathPoints());

        Wave wave = waveService.generateWave(0, paths);
        List<Player> players = users.stream()
                .map(user -> new Player(user.getId(), user.getProfile().getGameClass(), user.getNickname()))
                .collect(Collectors.toList());

        Map<String, PlayerClass> availableClasses = users.stream()
                .map(user -> resourceFactory.loadResource(user.getProfile().getGameClass() + ".json", PlayerClass.class))
                .collect(Collectors
                        .toMap(PlayerClass::toString,
                                value -> value,
                                (left, right) -> left));

        Map<Long, PlayerClass> playersClasses = users.stream()
                .collect(Collectors
                        .toMap(User::getId,
                                user -> availableClasses.get(user.getProfile().getGameClass())
                        ));

        GameParams sessionParams = gameSessionsParams.get(users.size() - 1);
        return new GameSession(players, playersClasses, map, wave, paths, sessionParams);

    }
}
