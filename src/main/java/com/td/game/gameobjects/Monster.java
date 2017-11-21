package com.td.game.gameobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.td.game.domain.PathPoint;
import com.td.game.domain.Point;
import com.td.game.resource.Resource;
import com.td.game.snapshots.Snapshot;
import com.td.game.snapshots.Snapshotable;

public class Monster extends GameObject implements Snapshotable<Monster> {

    private final Integer typeid;
    private int hp;
    private double speed;
    private PathPoint coord;
    private Point<Double> relativeCoord;

    private int reward;
    private int weight;
    private int vx;
    private int vy;

    public Monster(MonsterResource resource) {
        super();
        final double timeScale = 0.001;
        this.hp = resource.hp;
        this.speed = resource.speed * timeScale;
        this.weight = resource.weight;
        this.relativeCoord = new Point<>(0.0, 0.0);
        this.typeid = resource.typeid;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    @Override
    public MonsterSnapshot getSnapshot() {
        return new MonsterSnapshot(this);
    }


    public void move(long delta, PathPoint next) {
        double newx = relativeCoord.getXcoord() + delta * vx * speed;
        double newy = relativeCoord.getYcoord() + delta * vy * speed;
        if (vx != 0 && Math.abs(newx) >= 1) {
            if (newx <= -1) {
                newx += 1;
            } else {
                newx -= 1;
            }
            setCoord(next);
        } else if (vy != 0 && Math.abs(newy) >= 1) {
            if (newy <= -1) {
                newy += 1;
            } else if (newy >= 1) {
                newy -= 1;
            }
            setCoord(next);
        }
        relativeCoord.set(newx, newy);
    }

    public void setCoord(PathPoint coord) {
        this.coord = coord;
        this.vx = coord.getDirections().getXcoord();
        this.vy = coord.getDirections().getYcoord();
    }

    public void setRelativeCoord(Point<Double> relativeCoord) {
        this.relativeCoord = relativeCoord;
    }

    public void setVx(int vx) {
        this.vx = vx;
    }

    public void setVy(int vy) {
        this.vy = vy;
    }

    public PathPoint getCoord() {
        return coord;
    }

    public int getWeight() {
        return weight;
    }

    public int getReward() {
        return reward;
    }


    public class MonsterSnapshot implements Snapshot<Monster> {
        private Long id;
        private Integer typeid;
        private int hp;
        private double speed;
        private Point<Long> titleCoord;
        private Point<Double> relativeCoord;

        public Long getId() {
            return id;
        }

        public int getHp() {
            return hp;
        }

        public double getSpeed() {
            return speed;
        }

        public Point<Long> getTitleCoord() {
            return titleCoord;
        }

        public Point<Double> getRelativeCoord() {
            return relativeCoord;
        }

        public MonsterSnapshot(Monster monster) {
            final int timeScale = 1000;
            this.id = monster.getId();
            this.hp = monster.getHp();
            this.speed = monster.speed * timeScale;
            this.titleCoord = monster.coord.getTitleCoord();
            this.relativeCoord = monster.relativeCoord;
            this.typeid = monster.typeid;
        }

        public Integer getTypeid() {
            return typeid;
        }
    }

    public static class MonsterResource extends Resource {
        private int hp;
        private double speed;
        private int weight;
        private int reward;
        private Integer typeid;

        @JsonCreator
        MonsterResource(@JsonProperty("hp") int hp,
                        @JsonProperty("speed") double speed,
                        @JsonProperty("weight") int weight,
                        @JsonProperty("reward") int reward,
                        @JsonProperty("typeid") int typeid) {
            this.hp = hp;
            this.speed = speed;
            this.weight = weight;
            this.reward = reward;
            this.typeid = typeid;
        }


    }
}
