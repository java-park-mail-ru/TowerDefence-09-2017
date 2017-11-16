package com.td.game.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.td.game.gameobjects.GameObject;
import com.td.game.resource.Resource;
import com.td.game.snapshots.Snapshot;
import com.td.game.snapshots.Snapshotable;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GameMap implements Snapshotable<GameMap> {
    private Map<Point<Long>, Title> gameMap;
    private final long height;
    private final long width;

    public GameMap(GameMapResource resource) {
        this.width = resource.width;
        this.height = resource.height;
        System.out.println(resource.titles);
        this.gameMap = resource.titles
                .stream()
                .collect(Collectors.toMap(Title::getTitleCoord, Function.identity()));
    }

    public void setPathTitles(List<PathPoint> path) {
        for (PathPoint point : path) {
            Title title = point.getTitle();
            System.out.println(title);
            gameMap.compute(title.getTitleCoord(), (coord, current) -> {
                current.setTitleType(title.getTitleType());
                return current;
            });
        }

    }


    public Title getTitle(Point<Long> coords) {
        return gameMap.get(coords);
    }

    public long getHeight() {
        return height;
    }

    public long getWidth() {
        return width;
    }

    @Override
    public GameMapSnapshot getSnapshot() {
        return new GameMapSnapshot(this);
    }

    public boolean isTitleFree(long xcoord, long ycoord) {
        Title title = gameMap.get(new Point<>(xcoord, ycoord));
        return title != null && title.getOwner() == null;
    }

    public boolean placeObject(Point<Long> coord, GameObject obj) {
        Title title = gameMap.get(coord);
        if (title == null || title.getOwner() != null) {
            return false;
        }
        title.setOwner(obj);
        return true;
    }

    public void placeObject(long xcoord, long ycoord, GameObject gameObject) {
        placeObject(new Point<>(xcoord, ycoord), gameObject);
    }


    public class GameMapSnapshot implements Snapshot<GameMap> {
        private List<Title.TitleSnapshot> gameMap;
        private long height;
        private long width;

        public GameMapSnapshot(GameMap map) {
            this.gameMap = map.gameMap
                    .values()
                    .stream()
                    .map(Title::getSnapshot)
                    .collect(Collectors.toList());

            this.height = map.height;
            this.width = map.width;
        }

        public List<Title.TitleSnapshot> getGameMap() {
            return gameMap;
        }

        public long getHeight() {
            return height;
        }

        public long getWidth() {
            return width;
        }
    }

    public static class GameMapResource extends Resource {
        private List<Title> titles;
        private long height;
        private long width;

        @JsonCreator
        public GameMapResource(@JsonProperty("titles") List<Title> titles,
                               @JsonProperty("height") long height,
                               @JsonProperty("width") long width) {
            this.titles = titles;
            this.height = height;
            this.width = width;
        }


    }
}
