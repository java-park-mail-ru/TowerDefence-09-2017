package com.td.game.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.td.game.gameobjects.GameObject;
import com.td.game.snapshots.Snapshot;
import com.td.game.snapshots.Snapshotable;

public class Tile implements Snapshotable<Tile> {
    private long tileType;
    private GameObject owner;

    private Point<Long> tileCoord;

    public Point<Long> getTileCoord() {
        return tileCoord;
    }

    public void setTileCoord(Point<Long> tileCoord) {
        this.tileCoord = tileCoord;
    }

    public Tile(Point<Long> tileCoord, long tileType) {
        this.tileCoord = tileCoord;
        this.tileType = tileType;
    }

    @JsonCreator
    public Tile(@JsonProperty("x") long xc,
                @JsonProperty("y") long yc,
                @JsonProperty("tileType") long tileType) {
        this.tileCoord = new Point<>(xc, yc);
        this.tileType = tileType;
    }

    public Long getTileType() {
        return tileType;
    }

    public void setTileType(long tileType) {
        this.tileType = tileType;
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

        Tile tile = (Tile) obj;

        return tileCoord != null ? tileCoord.equals(tile.tileCoord) : tile.tileCoord == null;
    }

    @Override
    public int hashCode() {
        return tileCoord != null ? tileCoord.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Tile{"
                + "tileType=" + tileType
                + ", owner=" + owner
                + ", tileCoord=" + tileCoord
                + '}';
    }

    @Override
    public TileSnapshot getSnapshot() {
        return new TileSnapshot(this);
    }

    public static class TileSnapshot implements Snapshot<Tile> {
        @JsonProperty("x")
        private long xcoord;
        @JsonProperty("y")
        private long ycoord;
        private long tileType;

        public TileSnapshot(Tile tile) {
            Point<Long> coord = tile.getTileCoord();
            this.xcoord = coord.getXcoord();
            this.ycoord = coord.getYcoord();
            this.tileType = tile.getTileType();
        }

        public long getXcoord() {
            return xcoord;
        }

        public long getYcoord() {
            return ycoord;
        }

        public long getTileType() {
            return tileType;
        }
    }
}
