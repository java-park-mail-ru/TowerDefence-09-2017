package com.td.game.snapshots;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.td.websocket.Message;

public class TowerOrderMessage extends Message {
    private Long playerId;
    private Integer orderedTower;
    private long ycoord;
    private long xcoord;

    @JsonCreator
    TowerOrderMessage(@JsonProperty("playerId") Long playerId,
                      @JsonProperty("orderedTower") Integer orderedTower,
                      @JsonProperty("x") long xcoord,
                      @JsonProperty("y") long ycoord) {
        this.playerId = playerId;
        this.orderedTower = orderedTower;
        this.ycoord = ycoord;
        this.xcoord = xcoord;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public void setOrderedTower(Integer orderedTower) {
        this.orderedTower = orderedTower;
    }

    public Integer getOrderedTower() {
        return orderedTower;
    }

    @JsonProperty("y")
    public long getYcoord() {
        return ycoord;
    }

    @JsonProperty("x")
    public long getXcoord() {
        return xcoord;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setYcoord(long ycoord) {
        this.ycoord = ycoord;
    }

    public void setXcoord(long xcoord) {
        this.xcoord = xcoord;
    }
}
