package com.td.game.services;

import com.td.game.GameSession;
import com.td.game.domain.Player;
import com.td.game.domain.ShotEvent;
import com.td.game.gameobjects.Tower;
import com.td.game.snapshots.ServerSnap;
import com.td.websocket.TransportService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.stream.Collectors;

@Service
public class ServerSnaphotService {

    @NotNull
    private TransportService transportService;

    public ServerSnaphotService(@NotNull TransportService transportService) {
        this.transportService = transportService;
    }

    public void sendServerSnapshot(@NotNull GameSession session) {

        ServerSnap snap = ServerSnap.builder()
                .setPlayersSnap(session
                        .getPlayers()
                        .stream()
                        .map(Player::getSnapshot)
                        .collect(Collectors.toList()))
                .setTowersSnap(session
                        .getTowers()
                        .stream()
                        .map(Tower::getSnapshot)
                        .collect(Collectors.toList()))
                .setCurrentWaveSnap(session
                        .getCurrentWave()
                        .getSnapshot())
                .setShotEvents(session
                        .getShotEvents()
                        .stream()
                        .map(ShotEvent::getSnapshot)
                        .collect(Collectors.toList()))
                .setHp(session.getHp())
                .setWaveNumber(session.getWaveNumber())
                .build();
        for (Player player : session.getPlayers()) {
            try {
                snap.setPlayerId(player.getId());
                transportService.sendMessageToUser(player.getId(), snap);

            } catch (IOException e) {
                throw new SnapshotSendingException("Unable to send server snapshot to " + player.getId(), e);
            }
        }
    }


}
