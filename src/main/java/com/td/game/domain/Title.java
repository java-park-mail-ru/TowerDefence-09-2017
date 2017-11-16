package com.td.game.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.td.game.gameobjects.GameObject;
import com.td.game.snapshots.Snapshot;
import com.td.game.snapshots.Snapshotable;

public class Title implements Snapshotable<Title> {
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

    @JsonCreator
    public Title(@JsonProperty("x") long xc,
                 @JsonProperty("y") long yc,
                 @JsonProperty("titleType") long titleType) {
        this.titleCoord = new Point<>(xc, yc);
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Title title = (Title) obj;

        return titleCoord != null ? titleCoord.equals(title.titleCoord) : title.titleCoord == null;
    }

    @Override
    public int hashCode() {
        return titleCoord != null ? titleCoord.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Title{"
                + "titleType=" + titleType
                + ", owner=" + owner
                + ", titleCoord=" + titleCoord
                + '}';
    }

    @Override
    public TitleSnapshot getSnapshot() {
        return new TitleSnapshot(this);
    }

    public static class TitleSnapshot implements Snapshot<Title> {
        @JsonProperty("x")
        private long xcoord;
        @JsonProperty("y")
        private long ycoord;
        private long titleType;

        public TitleSnapshot(Title title) {
            Point<Long> coord = title.getTitleCoord();
            this.xcoord = coord.getXcoord();
            this.ycoord = coord.getYcoord();
            this.titleType = title.getTitleType();
        }

        public long getXcoord() {
            return xcoord;
        }

        public long getYcoord() {
            return ycoord;
        }

        public long getTitleType() {
            return titleType;
        }
    }
}
