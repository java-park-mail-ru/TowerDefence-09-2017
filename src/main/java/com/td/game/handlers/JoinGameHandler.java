package com.td.game.handlers;

import com.td.game.GameManager;
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
    private final GameManager gameManager;

    public JoinGameHandler(@NotNull MessageHandlersContainer messageHandlerContainer,
                           @NotNull GameManager gameManager) {
        super(JoinGameMessage.class);
        this.messageHandlerContainer = messageHandlerContainer;
        this.gameManager = gameManager;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(JoinGameMessage.class, this);
    }

    @Override
    public void handle(JoinGameMessage message, Long id) {
        gameManager.addUser(id);
    }
}
