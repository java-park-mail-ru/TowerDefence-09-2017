package com.td.game.gameobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.td.game.domain.PathPoint;
import com.td.game.resource.Resource;

import java.util.List;

public class Path extends GameObject {
    private final List<PathPoint> pathPoints;

    public Path(PathResource res) {
        super();
        this.pathPoints = res.points;
    }

    public PathPoint getInitalPoint() {
        return pathPoints.get(0);
    }

    public List<PathPoint> getPathPoints() {
        return pathPoints;
    }

    public Integer getLength() {
        return pathPoints.size();
    }

    public PathPoint getLastPoint() {
        return pathPoints.get(pathPoints.size() - 1);
    }

    public static class PathResource extends Resource {
        private final List<PathPoint> points;

        @JsonCreator
        public PathResource(@JsonProperty("points") List<PathPoint> points) {
            this.points = points;
        }
    }
}
