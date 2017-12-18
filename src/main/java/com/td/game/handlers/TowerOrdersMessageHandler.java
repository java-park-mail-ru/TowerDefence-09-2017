package com.td.game.handlers;


import com.td.game.GameScheduler;
import com.td.game.snapshots.TowerOrderMessage;
import com.td.websocket.MessageHandler;
import com.td.websocket.MessageHandlersContainer;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class TowerOrdersMessageHandler extends MessageHandler<TowerOrderMessage> {
    @NotNull
    private final GameScheduler gameScheduler;

    @NotNull
    private final MessageHandlersContainer messageHandlersContainer;

    @PostConstruct
    public void init() {
        messageHandlersContainer.registerHandler(TowerOrderMessage.class, this);
    }

    public TowerOrdersMessageHandler(@NotNull GameScheduler gameScheduler,
                                     @NotNull MessageHandlersContainer messageHandlersContainer) {
        super(TowerOrderMessage.class);
        this.gameScheduler = gameScheduler;
        this.messageHandlersContainer = messageHandlersContainer;
    }

    @Override
    public void handle(@NotNull TowerOrderMessage message, Long id) {
        gameScheduler.addTowerOrder(message.getXcoord(), message.getYcoord(),message.getOrderedTower(), id);
    }
}
