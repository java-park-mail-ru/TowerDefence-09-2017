package com.td.websocket;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MessageHandlersContainer {

    private final Map<Class<?>, MessageHandler<?>> handlerMap = new HashMap<>();

    public void handle(@NotNull Message message, Long id) {
        final MessageHandler<?> messageHandler = handlerMap.get(message.getClass());
        messageHandler.handleMessage(message, id);
    }

    public <T extends Message> void registerHandler(@NotNull Class<T> clazz, MessageHandler<T> handler) {
        handlerMap.put(clazz, handler);
    }

}
