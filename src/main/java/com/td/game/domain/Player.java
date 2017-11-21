package com.td.game.domain;

import com.td.game.snapshots.Snapshot;
import com.td.game.snapshots.Snapshotable;

public class Player implements Snapshotable<Player> {
    private final Long id;
    private long money;
    private int scores;
    private String playerClass;
    private int level;
    private static final int START_MONEY = 100;

    public Player(Long id, String gameClass) {
        this.id = id;
        this.money = START_MONEY;
        this.scores = 0;
        this.level = 0;
        this.playerClass = gameClass;

    }

    public Long getId() {
        return id;
    }


    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public int getScores() {
        return scores;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }

    public void updateScores(int scores) {
        this.scores += scores;
    }

    public PlayerSnapshot getSnapshot() {
        return new PlayerSnapshot(this);
    }

    public void updateMoney(int money) {
        this.money += money;
    }

    public class PlayerSnapshot implements Snapshot<Player> {
        private Long id;
        private long money;
        private int scores;
        private String playerClass;

        PlayerSnapshot(Player player) {
            this.id = player.id;
            this.money = player.money;
            this.scores = player.scores;
            this.playerClass = player.playerClass;
        }

        public Long getId() {
            return id;
        }

        public long getMoney() {
            return money;
        }

        public int getScores() {
            return scores;
        }

        public String getPlayerClass() {
            return playerClass;
        }
    }

}
