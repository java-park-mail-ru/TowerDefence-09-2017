package com.td.game.gameObjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.td.game.domain.Area;
import com.td.game.domain.Point;
import com.td.game.resourceSystem.Resource;
import com.td.game.snapshots.Snapshot;
import com.td.game.snapshots.Snapshotable;

import java.util.ArrayList;
import java.util.List;

public class Tower extends GameObject implements Snapshotable<Tower> {
    private int damage;
    private boolean ready;
    private long period;
    private long msSinceLastShot;
    private int range;
    private int cost;
    private Point<Long> titlePosition;

    public Tower(TowerResource resource) {
        this.damage = resource.damage;
        this.period = resource.period;
        this.range = resource.range;
        this.cost = resource.cost;
        this.titlePosition = new Point<>(0L, 0L);
        this.ready = true;
        this.msSinceLastShot = 0;
    }

    void reload(long delta) {
        if (isReady()) {
            return;
        }
        this.msSinceLastShot += delta;
        if (msSinceLastShot >= period) {
            msSinceLastShot -= period;
            this.ready = true;
        }
    }

    public void setTitlePosition(long x, long y) {
        this.titlePosition = new Point<>(x, y);
    }

    public boolean isReady() {
        return ready;
    }

    public List<ShotEvent> fire(long interval, Area area) {
        if (!isReady()) {
            return new ArrayList<>();
        }
        long shotsCount = Math.floorDiv(msSinceLastShot + interval, period);
        msSinceLastShot = (msSinceLastShot + interval) - period * shotsCount;
        long shotsDone = 0;
        List<ShotEvent> shots = new ArrayList<>();
        while (!area.isEmpty() && shotsDone < shotsCount) {
            Monster target = area.peekFirst();
            for (; shotsDone < shotsCount; ++shotsDone) {
                target.setHp(target.getHp() - damage);
                shots.add(new ShotEvent(target, this, shotsDone * period));
                if (target.getHp() <= 0) {
                    area.removeFirst();
                    break;
                }
            }
        }
        return shots;
    }


    public TowerSnapshot getSnapshot() {
        return new TowerSnapshot(this);
    }

    public class TowerSnapshot implements Snapshot<Tower> {
        private int damage;
        private long period;
        private int range;
        private int cost;
        private Point<Long> titlePosition;

        public TowerSnapshot(Tower tower) {
            this.damage = tower.damage;
            this.period = tower.period;
            this.range = tower.range;
            this.cost = tower.cost;
            this.titlePosition = tower.titlePosition;
        }

        public int getDamage() {
            return damage;
        }

        public long getPeriod() {
            return period;
        }

        public int getRange() {
            return range;
        }

        public int getCost() {
            return cost;
        }

        public Point<Long> getTitlePosition() {
            return titlePosition;
        }
    }

    public class TowerResource extends Resource {
        private int damage;
        private long period;
        private int range;
        private int cost;

        @JsonCreator
        public TowerResource(int damage, long period, int range, int cost) {
            this.damage = damage;
            this.period = period;
            this.range = range;
            this.cost = cost;
        }

        public int getDamage() {
            return damage;
        }

        public long getPeriod() {
            return period;
        }

        public int getRange() {
            return range;
        }

        public int getCost() {
            return cost;
        }
    }

    public void setPeriod(long period) {
        this.period = period;
    }
}
