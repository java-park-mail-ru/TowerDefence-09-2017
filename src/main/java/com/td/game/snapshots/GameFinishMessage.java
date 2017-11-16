package com.td.game.snapshots;

import com.td.websocket.Message;

public class GameFinishMessage extends Message {
    private final int scores;

    public GameFinishMessage(int scores) {
        this.scores = scores;
    }

    public int getScores() {
        return scores;
    }
}
