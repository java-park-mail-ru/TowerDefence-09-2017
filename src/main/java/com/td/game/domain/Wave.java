package com.td.game.domain;

import com.td.game.gameobjects.Monster;
import com.td.game.gameobjects.Path;
import com.td.game.snapshots.Snapshot;
import com.td.game.snapshots.Snapshotable;

import java.util.*;
import java.util.stream.Collectors;

public class Wave implements Snapshotable<Wave> {
    private Queue<Monster> pending;
    private Set<Monster> running = new HashSet<>();
    private List<Monster> passed = new ArrayList<>();

    private WaveStatus status;
    private long localTimeBuffer;
    private long monsterEnterDelay;

    private final Map<Path, List<Monster>> pathBindings;

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

    public Map<Path, List<Monster>> getPathBindings() {
        return pathBindings;
    }

    public WaveStatus getStatus() {
        return status;
    }

    public long getLocalTimeBuffer() {
        return localTimeBuffer;
    }

    public long getMonsterEnterDelay() {
        return monsterEnterDelay;
    }

    public enum WaveStatus {
        PENDING,
        STARTED,
        RUNNING,
        FINISHED
    }

    public Wave(List<Monster> monsters, Map<Path, List<Monster>> pathBindings, long startDealy, long monsterEnterDelay) {
        this.pending = new ArrayDeque<>(monsters);
        this.monsterEnterDelay = monsterEnterDelay;
        this.status = WaveStatus.PENDING;
        this.localTimeBuffer = startDealy;
        this.pathBindings = pathBindings;
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

    public void tryToFinishWave() {
        if (status == WaveStatus.FINISHED) {
            return;
        }
        if (status != WaveStatus.RUNNING) {
            return;
        }
        if (pending.isEmpty() && running.isEmpty()) {
            this.status = WaveStatus.FINISHED;
        }
    }

    public void passDeadMonsters() {
        Iterator<Monster> iter = running.iterator();
        while (iter.hasNext()) {
            Monster monster = iter.next();
            if (monster.getHp() <= 0) {
                iter.remove();
                passed.add(monster);
            }
        }
    }

    public static class WaveSnapshot implements Snapshot<Wave> {
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

        public List<Monster.MonsterSnapshot> getPending() {
            return pending;
        }

        public List<Monster.MonsterSnapshot> getRunning() {
            return running;
        }

        public List<Monster.MonsterSnapshot> getPassed() {
            return passed;
        }

        public String getStatus() {
            return status;
        }

        public long getMsToStart() {
            return msToStart;
        }
    }
}
