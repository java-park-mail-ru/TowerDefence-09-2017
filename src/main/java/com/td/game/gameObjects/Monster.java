package com.td.game.gameObjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.td.game.domain.Point;
import com.td.game.resourceSystem.Resource;
import com.td.game.services.TickerService;
import com.td.game.snapshots.Snapshot;
import com.td.game.snapshots.Snapshotable;

public class Monster extends GameObject implements Snapshotable<Monster> {

    private int hp;
    private double speed;
    private Point<Long> titleCoord;
    private Point<Double> relativeCoord;
    private int weight;
    private int vx;
    private int vy;
    private long lastTimeMoved;

    public Monster(MonsterResource resource) {
        this.hp = resource.hp;
        this.speed = resource.speed;
        this.weight = resource.weight;
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


    public void move(TickerService ticker) {
        long delta = ticker.getMs() - lastTimeMoved;
        double newx = relativeCoord.getX() + delta * vx;
        double newy = relativeCoord.getY() + delta * vy;
        if (Math.abs(newx) >= 1 || Math.abs(newy) >= 1) {
            relativeCoord.set(0.0, 0.0);
        }
        relativeCoord.set(newx, newy);
    }


    public class MonsterSnapshot implements Snapshot<Monster> {
        private Long id;
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
            this.id = monster.getId();
            this.hp = monster.getHp();
            this.speed = monster.speed;
            this.titleCoord = monster.titleCoord;
            this.relativeCoord = monster.relativeCoord;
        }
    }

    public class MonsterResource extends Resource {
        private int hp;
        private double speed;
        private int weight;

        @JsonCreator
        MonsterResource(int hp,
                        double speed,
                        int weight) {
            this.hp = hp;
            this.speed = speed;
            this.weight = weight;
        }

        public int getHp() {
            return hp;
        }

        public double getSpeed() {
            return speed;
        }

        public int getWeight() {
            return weight;
        }
    }
}
