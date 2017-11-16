package com.td.game.domain;

import com.td.game.gameobjects.Monster;
import com.td.game.gameobjects.Tower;
import com.td.game.snapshots.Snapshot;
import com.td.game.snapshots.Snapshotable;

public class ShotEvent implements Snapshotable<ShotEvent> {
    private final Long monsterId;
    private final Long towerId;
    private final Long offset;

    public ShotEvent(Monster monster, Tower tower, Long offset) {
        this.monsterId = monster.getId();
        this.towerId = tower.getId();
        this.offset = offset;
    }

    public Long getMonsterId() {
        return monsterId;
    }

    public Long getTowerId() {
        return towerId;
    }

    public Long getOffset() {
        return offset;
    }

    @Override
    public ShotEventSnapshot getSnapshot() {
        return new ShotEventSnapshot(this);
    }

    public class ShotEventSnapshot implements Snapshot<ShotEvent> {
        private final Long monsterId;
        private final Long towerId;
        private final Long offset;

        public ShotEventSnapshot(ShotEvent event) {
            this.monsterId = event.monsterId;
            this.towerId = event.towerId;
            this.offset = event.offset;
        }

        public Long getTowerId() {
            return towerId;
        }

        public Long getMonsterId() {
            return monsterId;
        }

        public Long getOffset() {
            return offset;
        }
    }
}
