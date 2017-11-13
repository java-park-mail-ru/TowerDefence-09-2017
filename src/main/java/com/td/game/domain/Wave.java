package com.td.game.domain;

import com.td.game.gameObjects.Monster;
import com.td.game.snapshots.Snapshot;
import com.td.game.snapshots.Snapshotable;

import java.util.*;
import java.util.stream.Collectors;

public class Wave implements Snapshotable<Wave> {
    private Queue<Monster> pending;
    private Set<Monster> running;
    private List<Monster> passed;

    private WaveStatus status;
    private long localTimeBuffer;
    private long monsterEnterDelay;

    public Queue<Monster> getPending() {
        return pending;
    }

    public Set<Monster> getRunning() {
        return running;
    }

    public List<Monster> getPassed() {
        return passed;
    }

    @Override
    public WaveSnapshot getSnapshot() {
        return new WaveSnapshot(this);
    }

    enum WaveStatus {
        PENDING,
        STARTED,
        RUNNING,
        FINISHED
    }

    public Wave(Queue<Monster> monsters, long startDealy, long monsterEnterDelay) {
        this.pending = monsters;
        this.monsterEnterDelay = monsterEnterDelay;
        this.running = new HashSet<>();
        this.passed = new ArrayList<>();
        this.status = WaveStatus.PENDING;
        this.localTimeBuffer = startDealy;
    }

    public void updatePendingTime(long ms) {
        if (status != WaveStatus.PENDING) {
            return;
        }
        localTimeBuffer -= ms;
        if (localTimeBuffer <= 0) {
            this.localTimeBuffer = Math.abs(localTimeBuffer);
            this.status = WaveStatus.STARTED;
        }
    }

    public void pollMonster(long ms) {
        if (status != WaveStatus.STARTED) {
            return;
        }
        localTimeBuffer += ms;
        if (localTimeBuffer >= monsterEnterDelay) {
            localTimeBuffer -= monsterEnterDelay;
            Monster monster = pending.poll();
            if (monster == null) {
                this.status = WaveStatus.RUNNING;
                return;
            }
            running.add(monster);
        }
    }

    public void passMonster(Monster monster) {
        if (!running.remove(monster)) {
            return;
        }
        passed.add(monster);
    }

    public boolean checkFinishCondition() {
        if (status != WaveStatus.RUNNING) {
            return false;
        }
        if (pending.isEmpty() && running.isEmpty()) {
            this.status = WaveStatus.FINISHED;
        }
        return true;
    }

    public class WaveSnapshot implements Snapshot<Wave> {
        private List<Monster.MonsterSnapshot> pending;
        private List<Monster.MonsterSnapshot> running;
        private List<Monster.MonsterSnapshot> passed;

        private String status;
        private long msToStart;

        public WaveSnapshot(Wave wave) {
            this.passed = wave.passed
                    .stream()
                    .map(Monster::getSnapshot)
                    .collect(Collectors.toList());

            this.pending = wave.pending
                    .stream()
                    .map(Monster::getSnapshot)
                    .collect(Collectors.toList());

            this.running = wave.running
                    .stream()
                    .map(Monster::getSnapshot)
                    .collect(Collectors.toList());

            this.status = wave.status.toString().toLowerCase();
            if (wave.status == WaveStatus.PENDING) {
                this.msToStart = wave.localTimeBuffer;
            } else {
                this.msToStart = 0;
            }
        }

    }
}
