package com.td.game.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PathPoint {

    private Point<Integer> directions;
    private Title title;
    private Integer index;

    @JsonCreator
    public PathPoint(@JsonProperty("x") long xcoord,
                     @JsonProperty("y") long ycoord,
                     @JsonProperty("titleType") long titleType,
                     @JsonProperty("vx") int vx,
                     @JsonProperty("vy") int vy,
                     @JsonProperty("index") Integer index) {
        this.index = index;
        this.directions = new Point<>(vx, vy);
        this.title = new Title(xcoord, ycoord, titleType);
    }

    public Point<Integer> getDirections() {
        return directions;
    }

    public Title getTitle() {
        return title;
    }

    public Point<Long> getTitleCoord() {
        return title.getTitleCoord();
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

        return title != null ? title.equals(pathPoint.title) : pathPoint.title == null;
    }

    @Override
    public int hashCode() {
        return title != null ? title.hashCode() : 0;
    }

    public Integer getIndex() {
        return index;
    }
}
