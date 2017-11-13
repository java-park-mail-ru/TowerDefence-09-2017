package com.td.game;

import com.td.game.domain.Player;
import com.td.game.domain.PlayerClass;
import com.td.game.domain.Wave;
import com.td.game.domain.GameMap;
import com.td.game.gameObjects.ShotEvent;
import com.td.game.gameObjects.Tower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

public class GameSession {
    static final private AtomicLong ID_SOURCE = new AtomicLong(0);

    private final long id;
    private final ArrayList<Player> players;
    private final HashMap<Long, PlayerClass> playersClasses;
    private final GameMap gameMap;
    private final ArrayList<Tower> towers;
    private final Wave currentWave;
    private int waveNumber = 0;
    private final ArrayList<ShotEvent> shots;

    public GameSession(ArrayList<Player> players,
                       HashMap<Long, PlayerClass> playersClasses,
                       GameMap gameMap,
                       Wave currentWave,
                       ArrayList<Tower> towers,
                       ArrayList<ShotEvent> shots) {
        this.players = players;
        this.playersClasses = playersClasses;
        this.id = ID_SOURCE.getAndIncrement();
        this.gameMap = gameMap;
        this.towers = towers;
        this.shots = shots;
        this.currentWave = currentWave;
    }


    public static AtomicLong getIdSource() {
        return ID_SOURCE;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public GameMap getGameMap() {
        return gameMap;
    }


    public Wave getCurrentWave() {
        return currentWave;
    }

    public ArrayList<Tower> getTowers() {
        return towers;
    }

    public ArrayList<ShotEvent> getShots() {
        return shots;
    }

    public int getWaveNumber() {
        return waveNumber;
    }

    public void setWaveNumber(int waveNumber) {
        this.waveNumber = waveNumber;
    }

    public HashMap<Long, PlayerClass> getPlayersClasses() {
        return playersClasses;
    }
}
