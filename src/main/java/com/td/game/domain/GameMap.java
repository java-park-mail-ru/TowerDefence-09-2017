package com.td.game.domain;

import com.td.game.gameObjects.*;
import com.td.game.snapshots.Snapshot;
import com.td.game.snapshots.Snapshotable;

import java.util.HashMap;
import java.util.List;

public class GameMap implements Snapshotable<GameMap> {
    private HashMap<Point<Long>, Title> gameMap;
    private final long height;
    private final long width;

    public GameMap(long height, long width) {
        this.width = width;
        this.height = height;
        for (long i = 0; i < height; ++i) {
            for (long j = 0; j < width; ++j) {
                Point<Long> coord = new Point<>(j, i);
                gameMap.put(coord, new Title(coord, 0));
            }
        }
    }

    public void addPath(List<Title> path) {
        for (Title title : path) {
            gameMap.compute(title.getTitleCoord(), (coord, current) -> {
                current.setTitleType(title.getTitleType());
                return current;
            });
        }
    }

    public boolean placeObject(Point<Long> coord, GameObject obj) {
        Title title = gameMap.get(coord);
        if (title == null || title.getOwner() != null) {
            return false;
        }
        title.setOwner(obj);
        return true;
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

    public class GameMapSnapshot implements Snapshot<GameMap> {
        private HashMap<Point<Long>, Title> gameMap;
        private final long height;
        private final long width;

        public GameMapSnapshot(GameMap map) {
            this.gameMap = map.gameMap;
            this.height = map.height;
            this.width = map.width;
        }

        public HashMap<Point<Long>, Title> getGameMap() {
            return gameMap;
        }

        public long getHeight() {
            return height;
        }

        public long getWidth() {
            return width;
        }
    }
}
