package com.td.game.services;

import com.td.game.GameContext;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameContextService {
    private final Map<Long, GameContext> userGameContext;

    GameContextService() {
        this.userGameContext = new ConcurrentHashMap<>();
    }

    public void registerUserMapping(Long id, GameContext context) {
        userGameContext.putIfAbsent(id, context);
    }

    public void unregisterUserMapping(Long id) {
        userGameContext.remove(id);
    }

    public GameContext getContext(Long id) {
        return userGameContext.get(id);
    }


}
