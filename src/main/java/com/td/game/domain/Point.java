package com.td.game.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Point<T extends Number> {
    private T xcoord;
    private T ycoord;

    @JsonProperty("x")
    public T getXcoord() {
        return xcoord;
    }

    public void setXcoord(T xcoord) {
        this.xcoord = xcoord;
    }

    @JsonProperty("y")
    public T getYcoord() {
        return ycoord;
    }

    public void setYcoord(T ycoord) {
        this.ycoord = ycoord;
    }

    public void set(T xc, T yc) {
        xcoord = xc;
        ycoord = yc;
    }

    public Point(T xcoord, T ycoord) {
        this.xcoord = xcoord;
        this.ycoord = ycoord;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Point<?> point = (Point<?>) obj;

        if (xcoord != null ? !xcoord.equals(point.xcoord) : point.xcoord != null) {
            return false;
        }
        return ycoord != null ? ycoord.equals(point.ycoord) : point.ycoord == null;
    }

    @Override
    public int hashCode() {
        int result = xcoord != null ? xcoord.hashCode() : 0;
        final int magic = 31;
        result = magic * result + (ycoord != null ? ycoord.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Point{"
                + "xcoord=" + xcoord
                + ", ycoord=" + ycoord
                + '}';
    }
}
