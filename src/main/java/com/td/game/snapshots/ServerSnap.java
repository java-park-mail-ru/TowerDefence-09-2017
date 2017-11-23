package com.td.game.snapshots;

import com.td.game.domain.Player;
import com.td.game.domain.ShotEvent;
import com.td.game.domain.Wave;
import com.td.game.gameobjects.Tower;
import com.td.websocket.Message;

import java.util.List;

public class ServerSnap extends Message {

    private List<Player.PlayerSnapshot> players;
    private List<Tower.TowerSnapshot> towers;
    private Wave.WaveSnapshot currentWave;
    private List<ShotEvent.ShotEventSnapshot> shotEvents;
    private int waveNumber;
    private int hp;
    private long playerId;

    public ServerSnap() {

    }

    public static ServerSnapBuilder builder() {
        return new ServerSnap().new ServerSnapBuilder();
    }

    public List<Player.PlayerSnapshot> getPlayers() {
        return players;
    }

    public List<Tower.TowerSnapshot> getTowers() {
        return towers;
    }

    public Wave.WaveSnapshot getCurrentWave() {
        return currentWave;
    }

    public List<ShotEvent.ShotEventSnapshot> getShotEvents() {
        return shotEvents;
    }

    public int getWaveNumber() {
        return waveNumber;
    }

    public int getHp() {
        return hp;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public final class ServerSnapBuilder {
        public ServerSnapBuilder setPlayersSnap(List<Player.PlayerSnapshot> playerSnapshots) {
            players = playerSnapshots;
            return this;
        }

        public ServerSnapBuilder setTowersSnap(List<Tower.TowerSnapshot> towersSnapshot) {
            towers = towersSnapshot;
            return this;
        }

        public ServerSnapBuilder setCurrentWaveSnap(Wave.WaveSnapshot waveSnap) {
            currentWave = waveSnap;
            return this;
        }

        public ServerSnapBuilder setShotEvents(List<ShotEvent.ShotEventSnapshot> shotEventsSnaps) {

            shotEvents = shotEventsSnaps;
            return this;
        }

        public ServerSnapBuilder setWaveNumber(int number) {
            waveNumber = number;
            return this;
        }

        public ServerSnapBuilder setHp(int healthpoints) {
            hp = healthpoints;
            return this;
        }

        public ServerSnap build() {
            return ServerSnap.this;
        }
    }
}
