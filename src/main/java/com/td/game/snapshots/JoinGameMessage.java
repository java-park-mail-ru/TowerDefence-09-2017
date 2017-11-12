package com.td.game.snapshots;

import com.td.websocket.Message;

public class JoinGameMessage extends Message {
    private String message;

    public JoinGameMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
