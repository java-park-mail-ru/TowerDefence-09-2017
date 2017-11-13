package com.td.game.domain;

import com.td.game.snapshots.Snapshot;
import com.td.game.snapshots.Snapshotable;

public class Player implements Snapshotable<Player> {
    private Long id;
    private long money;
    private long scores;
    private String gameClass;
    private int level;


    Player(Long id) {
        this.id = id;
        this.money = 100;
        this.scores = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public long getScores() {
        return scores;
    }

    public void setScores(long scores) {
        this.scores = scores;
    }

    public PlayerSnapshot getSnapshot() {
        return new PlayerSnapshot(this);
    }

    public class PlayerSnapshot implements Snapshot<Player> {
        private Long id;
        private long money;
        private long scores;

        PlayerSnapshot(Player player) {
            this.id = player.id;
            this.money = player.money;
            this.scores = player.scores;
        }

        public Long getId() {
            return id;
        }

        public long getMoney() {
            return money;
        }

        public long getScores() {
            return scores;
        }

    }

}
