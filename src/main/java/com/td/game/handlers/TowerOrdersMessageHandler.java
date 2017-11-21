package com.td.game.handlers;


import com.td.game.Game;
import com.td.game.snapshots.TowerOrderMessage;
import com.td.websocket.MessageHandler;
import com.td.websocket.MessageHandlersContainer;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class TowerOrdersMessageHandler extends MessageHandler<TowerOrderMessage> {
    @NotNull
    private final Game game;

    @NotNull
    private final MessageHandlersContainer messageHandlersContainer;

    @PostConstruct
    public void init() {
        messageHandlersContainer.registerHandler(TowerOrderMessage.class, this);
    }

    public TowerOrdersMessageHandler(@NotNull Game game,
                                     @NotNull MessageHandlersContainer messageHandlersContainer) {
        super(TowerOrderMessage.class);
        this.game = game;
        this.messageHandlersContainer = messageHandlersContainer;
    }

    @Override
    public void handle(@NotNull TowerOrderMessage message, Long id) {
        game.addTowerOrders(message.getOrderedTower(), message.getXcoord(), message.getYcoord(), id);
    }
}
