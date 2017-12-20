package com.td.game;


import com.td.game.services.GameContextService;
import com.td.game.services.TowerManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.StampedLock;

@Service
public class GameScheduler {

    private final int concurrencyLevel = Runtime.getRuntime().availableProcessors();

    private final List<GameContext> gameContexts;

    private final ExecutorService executorService;

    private final StampedLock queueLock;

    @NotNull
    private final GameContextService gameContextService;

    @NotNull
    private GameManager gameManager;
    private final Logger logger = LoggerFactory.getLogger(GameScheduler.class);


    public GameScheduler(@NotNull GameContextService gameContextService,
                         @NotNull GameManager gameManager) {
        this.gameContextService = gameContextService;
        this.gameManager = gameManager;

        this.gameContexts = new ArrayList<>();
        executorService = Executors.newFixedThreadPool(concurrencyLevel);
        queueLock = new StampedLock();
    }

    @EventListener
    void runExecutors(ContextRefreshedEvent event) {

        logger.info("Recieved event: {}", event);
        for (int i = 0; i < concurrencyLevel; ++i) {
            GameContext context = new GameContext(queueLock);
            gameContexts.add(context);
            executorService.submit(new GameExecutor(gameManager, context));
        }
        logger.info("Game executros number: {}", concurrencyLevel);
    }

    public void addWaiter(Long id) {
        long stamp = queueLock.writeLock();
        try {
            GameContext context = Collections.min(gameContexts, (lhs, rhs) -> {
                int sc = Integer.compare(lhs.getSessionsSize(), rhs.getSessionsSize());
                if (sc == 0) {
                    return Integer.compare(rhs.getWaitersQueueLength(), lhs.getWaitersQueueLength());
                }
                return sc;
            });
            context.addWaiter(id);
            logger.info("S:{}, W:{}", context.getSessionsSize(), context.getWaitersQueueLength());
        } finally {
            queueLock.unlockWrite(stamp);
        }

    }

    public void addTowerOrder(long xcoord, long ycoord, Integer towerClass, Long playerId) {
        GameContext context = gameContextService.getContext(playerId);
        context.addTowerOrder(new TowerManager.TowerOrder(xcoord, ycoord, towerClass, playerId));
    }

}
