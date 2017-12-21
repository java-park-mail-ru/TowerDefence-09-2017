package com.td.game.snapshots;

public interface Snapshotable<T> {
    Snapshot<T> getSnapshot();
}
