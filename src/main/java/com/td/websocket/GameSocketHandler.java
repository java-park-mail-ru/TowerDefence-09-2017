package com.td.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.td.daos.UserDao;
import com.td.domain.User;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

import static com.td.Constants.USER_SESSION_KEY;


public class GameSocketHandler extends TextWebSocketHandler {
    private static final Logger log = LoggerFactory.getLogger(GameSocketHandler.class);
    private static final CloseStatus UNAUTHORIZED = new CloseStatus(4001, "Unauthorized");

    @NotNull
    private final UserDao userService;

    @NotNull
    private final MessageHandlersContainer handlers;

    @NotNull
    private final TransportService sessions;

    @NotNull
    private final ObjectMapper mapper;


    public GameSocketHandler(@NotNull UserDao userService,
                             @NotNull MessageHandlersContainer handlers,
                             @NotNull TransportService sessions,
                             @NotNull ObjectMapper mapper) {
        this.userService = userService;
        this.handlers = handlers;
        this.sessions = sessions;
        this.mapper = mapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) {
        System.out.println("conntection established");
        final Long id = (Long) webSocketSession.getAttributes().get(USER_SESSION_KEY);
        if (id == null || userService.getUserById(id) == null) {
            closeSession(webSocketSession, UNAUTHORIZED);
        }
        sessions.registerUser(id, webSocketSession);
    }

    @Override
    protected void handleTextMessage(WebSocketSession webSocketSession, TextMessage message) {
        if (!webSocketSession.isOpen()) {
            return;
        }
        final Long id = (Long) webSocketSession.getAttributes().get(USER_SESSION_KEY);
        final User user;
        if (id == null || (user = userService.getUserById(id)) == null) {
            closeSession(webSocketSession, UNAUTHORIZED);
            return;
        }
        handle(user, message);

    }

    private void handle(User user, TextMessage rawMessage) {
        final Message message;
        try {
            message = mapper.readValue(rawMessage.getPayload(), Message.class);
        } catch (IOException ex) {
            log.error("Invalid message: {}", ex);
            return;
        }
        handlers.handle(message, user.getId());
    }

    private void closeSession(WebSocketSession session, CloseStatus status) {
        try {
            session.close(status);
        } catch (IOException e) {
            log.error("Error on closing session: {}", e);
        }

    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) {
        final Long userId = (Long) webSocketSession.getAttributes().get(USER_SESSION_KEY);
        sessions.removeUser(userId);
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) {
        log.error("Websocket transport error: {}", throwable);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
