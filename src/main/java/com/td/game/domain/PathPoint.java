package com.td.game.domain;

import com.td.game.gameObjects.Title;

public class PathPoint {
    private Point<Integer> directions;
    private Title title;

    public PathPoint(long x, long y, long titleType, int vx, int vy) {
        this.directions = new Point<>(vx, vy);
        this.title = new Title(x, y, titleType);
    }

    public Point<Integer> getDirections() {
        return directions;
    }

    public Title getTitle() {
        return title;
    }
}
