package com.td.game.handlers;

import com.td.game.snapshots.JoinGameMessage;
import com.td.websocket.MessageHandler;
import com.td.websocket.MessageHandlersContainer;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class JoinGameHandler extends MessageHandler<JoinGameMessage> {

    @NotNull
    private MessageHandlersContainer messageHandlerContainer;

    public JoinGameHandler(@NotNull MessageHandlersContainer messageHandlerContainer) {
        super(JoinGameMessage.class);
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(JoinGameMessage.class, this);
    }

    @Override
    public void handle(JoinGameMessage message, Long id) {
        System.out.println(id);
        System.out.println(message.getMessage());
    }
}
