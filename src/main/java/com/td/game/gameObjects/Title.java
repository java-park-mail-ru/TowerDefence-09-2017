package com.td.game.gameObjects;

import com.td.game.domain.Point;

public class Title {
    private long titleType;
    private GameObject owner;

    private Point<Long> titleCoord;

    public Point<Long> getTitleCoord() {
        return titleCoord;
    }

    public void setTitleCoord(Point<Long> titleCoord) {
        this.titleCoord = titleCoord;
    }

    public Title(Point<Long> titleCoord, long titleType) {
        this.titleCoord = titleCoord;
        this.titleType = titleType;
    }

    public Title(long x, long y, long titleType) {
        this.titleCoord = new Point<>(x, y);
        this.titleType = titleType;
    }

    public Long getTitleType() {
        return titleType;
    }

    public void setTitleType(long titleType) {
        this.titleType = titleType;
    }

    public GameObject getOwner() {
        return owner;
    }

    public void setOwner(GameObject owner) {
        this.owner = owner;
    }
}
