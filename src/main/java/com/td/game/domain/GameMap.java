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
    private Map<Point<Long>, Tile> gameMap;
    private final long height;
    private final long width;

    public GameMap(GameMapResource resource) {
        this.width = resource.width;
        this.height = resource.height;
        this.gameMap = resource.tiles
                .stream()
                .collect(Collectors.toMap(Tile::getTileCoord, Function.identity()));
    }

    public void setPathTiles(List<PathPoint> path) {
        for (PathPoint point : path) {
            Tile tile = point.getTile();
            gameMap.compute(tile.getTileCoord(), (coord, current) -> {
                current.setTileType(tile.getTileType());
                return current;
            });
        }

    }


    public Tile getTile(Point<Long> coords) {
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

    public boolean isTileFree(long xcoord, long ycoord) {
        Tile tile = gameMap.get(new Point<>(xcoord, ycoord));
        return tile != null && tile.getOwner() == null;
    }

    public boolean placeObject(Point<Long> coord, GameObject obj) {
        Tile tile = gameMap.get(coord);
        if (tile == null || tile.getOwner() != null) {
            return false;
        }
        tile.setOwner(obj);
        return true;
    }

    public boolean placeObject(long xcoord, long ycoord, GameObject gameObject) {
        return placeObject(new Point<>(xcoord, ycoord), gameObject);
    }


    public class GameMapSnapshot implements Snapshot<GameMap> {
        private List<Tile.TileSnapshot> gameMap;
        private long height;
        private long width;

        public GameMapSnapshot(GameMap map) {
            this.gameMap = map.gameMap
                    .values()
                    .stream()
                    .map(Tile::getSnapshot)
                    .collect(Collectors.toList());

            this.height = map.height;
            this.width = map.width;
        }

        public List<Tile.TileSnapshot> getGameMap() {
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
        private List<Tile> tiles;
        private long height;
        private long width;

        @JsonCreator
        public GameMapResource(@JsonProperty("tiles") List<Tile> tiles,
                               @JsonProperty("height") long height,
                               @JsonProperty("width") long width) {
            this.tiles = tiles;
            this.height = height;
            this.width = width;
        }


    }
}
