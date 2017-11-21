package com.td.game.snapshots;

import com.td.game.domain.GameMap;
import com.td.game.domain.Player;
import com.td.game.domain.TextureAtlas;
import com.td.game.domain.Wave;
import com.td.websocket.Message;

import java.util.List;

public class GameInitMessage extends Message {
    private Long playerId;
    private GameMap.GameMapSnapshot map;
    private List<Player.PlayerSnapshot> players;
    private List<Integer> availableTowers;
    private Wave.WaveSnapshot currentWave;
    private TextureAtlas textureAtlas;
    private Integer hp;

    public GameInitMessage(Long playerId,
                           GameMap.GameMapSnapshot map,
                           List<Player.PlayerSnapshot> players,
                           List<Integer> availableTowers,
                           Wave.WaveSnapshot currentWave,
                           TextureAtlas textureAtlas,
                           Integer hp) {
        this.playerId = playerId;
        this.hp = hp;
        this.map = map;
        this.players = players;
        this.availableTowers = availableTowers;
        this.currentWave = currentWave;
        this.textureAtlas = textureAtlas;
    }

    public GameMap.GameMapSnapshot getMap() {
        return map;
    }

    public List<Player.PlayerSnapshot> getPlayers() {
        return players;
    }

    public List<Integer> getAvailableTowers() {
        return availableTowers;
    }

    public Wave.WaveSnapshot getCurrentWave() {
        return currentWave;
    }

    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public Integer getHp() {
        return hp;
    }
}
