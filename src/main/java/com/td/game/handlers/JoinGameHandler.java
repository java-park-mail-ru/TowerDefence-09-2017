package com.td.game.handlers;

import com.td.game.GameScheduler;
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
    private final GameScheduler gameScheduler;

    public JoinGameHandler(@NotNull MessageHandlersContainer messageHandlerContainer,
                           @NotNull GameScheduler gameScheduler) {
        super(JoinGameMessage.class);
        this.messageHandlerContainer = messageHandlerContainer;
        this.gameScheduler = gameScheduler;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(JoinGameMessage.class, this);
    }

    @Override
    public void handle(JoinGameMessage message, Long id) {
        gameScheduler.addWaiter(id);
    }
}
