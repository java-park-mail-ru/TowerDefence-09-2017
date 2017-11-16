package com.td.game.handlers;

import com.td.game.Game;
import com.td.game.snapshots.JoinGameMessage;
import com.td.websocket.MessageHandler;
import com.td.websocket.MessageHandlersContainer;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class JoinGameHandler extends MessageHandler<JoinGameMessage> {

    @NotNull
    private final MessageHandlersContainer messageHandlerContainer;

    @NotNull
    private final Game game;

    public JoinGameHandler(@NotNull MessageHandlersContainer messageHandlerContainer,
                           @NotNull Game game) {
        super(JoinGameMessage.class);
        this.messageHandlerContainer = messageHandlerContainer;
        this.game = game;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(JoinGameMessage.class, this);
    }

    @Override
    public void handle(JoinGameMessage message, Long id) {

        game.addUser(id);
    }
}
