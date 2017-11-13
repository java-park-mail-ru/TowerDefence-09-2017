package com.td.game.snapshots;

import com.td.game.domain.GameMap;
import com.td.game.domain.Player;
import com.td.game.domain.Wave;
import com.td.game.gameObjects.Tower;
import com.td.websocket.Message;

import java.util.List;

public class GameInitMessage extends Message {
    private GameMap.GameMapSnapshot map;
    private List<Player.PlayerSnapshot> players;
    private List<Tower.TowerSnapshot> availableTowers;
    private Wave.WaveSnapshot currentWave;

    public GameInitMessage(GameMap.GameMapSnapshot map,
                           List<Player.PlayerSnapshot> players,
                           List<Tower.TowerSnapshot> availableTowers,
                           Wave.WaveSnapshot currentWave) {
        this.map = map;
        this.players = players;
        this.availableTowers = availableTowers;
        this.currentWave = currentWave;
    }

    public GameMap.GameMapSnapshot getMap() {
        return map;
    }

    public List<Player.PlayerSnapshot> getPlayers() {
        return players;
    }

    public List<Tower.TowerSnapshot> getAvailableTowers() {
        return availableTowers;
    }

    public Wave.WaveSnapshot getCurrentWave() {
        return currentWave;
    }
}
