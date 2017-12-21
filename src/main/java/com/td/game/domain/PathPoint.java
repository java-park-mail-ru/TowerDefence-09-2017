package com.td.game.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PathPoint {

    private Point<Integer> directions;
    private Tile tile;
    private Integer index;

    @JsonCreator
    public PathPoint(@JsonProperty("x") long xcoord,
                     @JsonProperty("y") long ycoord,
                     @JsonProperty("tileType") long tileType,
                     @JsonProperty("vx") int vx,
                     @JsonProperty("vy") int vy,
                     @JsonProperty("index") Integer index) {
        this.index = index;
        this.directions = new Point<>(vx, vy);
        this.tile = new Tile(xcoord, ycoord, tileType);
    }

    public Point<Integer> getDirections() {
        return directions;
    }

    public Tile getTile() {
        return tile;
    }

    public Point<Long> getTileCoord() {
        return tile.getTileCoord();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        PathPoint pathPoint = (PathPoint) obj;

        return tile != null ? tile.equals(pathPoint.tile) : pathPoint.tile == null;
    }

    @Override
    public int hashCode() {
        return tile != null ? tile.hashCode() : 0;
    }

    public Integer getIndex() {
        return index;
    }
}
