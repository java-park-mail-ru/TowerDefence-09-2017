package com.td.websocket;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.td.game.snapshots.*;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = GameInitMessage.class, name = "init"),
        @JsonSubTypes.Type(value = JoinGameMessage.class, name = "join"),
        @JsonSubTypes.Type(value = GameFinishMessage.class, name = "finish"),
        @JsonSubTypes.Type(value = ServerSnap.class, name = "state"),
        @JsonSubTypes.Type(value = ClientSnap.class),
})
public abstract class Message {
}
