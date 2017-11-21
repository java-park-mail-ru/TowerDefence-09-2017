package com.td.game;

import com.td.game.domain.*;
import com.td.game.gameobjects.Path;
import com.td.game.gameobjects.Tower;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class GameSession {
    private static final AtomicLong ID_SOURCE = new AtomicLong(0);
    private static final int BASE_HP = 100;
    private final long id;
    private final List<Player> players;
    private final Map<Long, PlayerClass> playersClasses;

    private final GameMap gameMap;
    private final List<Tower> towers;
    private final List<Area> areas;
    private Wave currentWave;
    private int waveNumber = 0;
    private int hp = BASE_HP;


    private final List<ShotEvent> shots;

    private final List<Path> paths;
    private List<ShotEvent> shotEvents;

    public GameSession(List<Player> players,
                       Map<Long, PlayerClass> playersClasses,
                       GameMap gameMap,
                       Wave currentWave,
                       List<Path> paths) {
        this.players = players;
        this.playersClasses = playersClasses;
        this.id = ID_SOURCE.getAndIncrement();
        this.gameMap = gameMap;
        this.towers = new ArrayList<>();
        this.shots = new ArrayList<>();
        this.areas = new ArrayList<>();
        this.currentWave = currentWave;
        this.paths = paths;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public Wave getCurrentWave() {
        return currentWave;
    }

    public List<Tower> getTowers() {
        return towers;
    }

    public List<ShotEvent> getShots() {
        return shots;
    }

    public int getWaveNumber() {
        return waveNumber;
    }

    public void setWaveNumber(int waveNumber) {
        this.waveNumber = waveNumber;
    }

    public Map<Long, PlayerClass> getPlayersClasses() {
        return playersClasses;
    }

    public boolean isFinished() {
        return hp <= 0;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        GameSession that = (GameSession) obj;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        final int magic = 32;
        return (int) (id ^ (id >>> magic));
    }

    public int getHp() {
        return hp;
    }

    public void setCurrentWave(Wave currentWave) {
        this.currentWave = currentWave;
    }

    public List<Path> getPaths() {
        return paths;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public void addArea(Area area) {
        areas.add(area);
    }

    public void setShotEvents(List<ShotEvent> shotEvents) {
        this.shotEvents = shotEvents;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public List<ShotEvent> getShotEvents() {
        return shotEvents;
    }
}
