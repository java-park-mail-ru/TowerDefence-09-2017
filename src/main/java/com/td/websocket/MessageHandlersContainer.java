package com.td.websocket;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MessageHandlersContainer {
    @NotNull
    private static final Logger log = LoggerFactory.getLogger(MessageHandlersContainer.class);

    private final Map<Class<?>, MessageHandler<?>> handlerMap = new HashMap<>();

    public void handle(@NotNull Message message, Long id) {
        final MessageHandler<?> messageHandler = handlerMap.get(message.getClass());
        messageHandler.handleMessage(message, id);
    }

    public <T extends Message> void registerHandler(@NotNull Class<T> clazz, MessageHandler<T> handler) {
        handlerMap.put(clazz, handler);
    }

}
