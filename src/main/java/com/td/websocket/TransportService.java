package com.td.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TransportService {

    private final Logger log = LoggerFactory.getLogger(TransportService.class);

    private Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper mapper;

    public TransportService(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public void registerUser(Long id, WebSocketSession session) {
        sessions.put(id, session);
    }

    public boolean isConnected(Long id) {
        return sessions.containsKey(id) && sessions.get(id).isOpen();
    }

    public void flushSession(Long id) {
        sessions.remove(id);
    }

    public void closeSession(Long id, CloseStatus status) {
        final WebSocketSession session = sessions.get(id);
        if (session != null && session.isOpen()) {
            try {
                session.close(status);
            } catch (IOException e) {
                log.error("Error on session closing: {}", e);
            }
        }
    }

    public void sendMessageToUser(Long id, Message message) throws IOException {
        final WebSocketSession session = sessions.get(id);
        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        }
        throw new IOException("Fail on message send: session for " + id + " doesn't exists");
    }

    public void removeUser(Long userId) {
        sessions.remove(userId);
    }
}
