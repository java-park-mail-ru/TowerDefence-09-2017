package com.td.game;


import com.td.game.services.GameContextService;
import com.td.game.services.TowerManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class GameScheduler {

    private final int CONCURRENCY_LEVEL = Runtime.getRuntime().availableProcessors();

    private final PriorityQueue<GameContext> gameContexts;

    private final ExecutorService executorService;

    @NotNull
    private final GameContextService gameContextService;

    @NotNull
    private GameManager gameManager;
    private final Logger logger = LoggerFactory.getLogger(GameScheduler.class);

    public GameScheduler(@NotNull GameContextService gameContextService,
                         @NotNull GameManager gameManager) {
        this.gameContextService = gameContextService;
        this.gameManager = gameManager;
        Comparator<GameContext> comp = Comparator
                .comparingInt(GameContext::getSessionsSize)
                .reversed()
                .thenComparing(GameContext::getWaitersQueueLength)
                .reversed();
        gameContexts = new PriorityQueue<>(comp);
        executorService = Executors.newFixedThreadPool(CONCURRENCY_LEVEL);
    }

    @PostConstruct
    void runExecutors() {
        logger.info("CONCURRENCY LEVEL: {}", CONCURRENCY_LEVEL);
        for (int i = 0; i < CONCURRENCY_LEVEL; ++i) {
            GameContext context = new GameContext();
            gameContexts.offer(context);
            executorService.submit(new GameExecutor(gameManager, context));
        }
    }

    public void addWaiter(Long id) {
        synchronized (gameContexts) {
            logger.info("Waiter {} comes to queue", id);
            GameContext context = gameContexts.poll();
            context.addWaiter(id);
            logger.info("S: {}, W: {}", context.getSessions().size(), context.getWaiters().size());
            gameContexts.offer(context);
        }
    }

    public void addTowerOrder(long xcoord, long ycoord, Integer towerClass, Long playerId) {
        GameContext context = gameContextService.getContext(playerId);
        context.addTowerOrder(new TowerManager.TowerOrder(xcoord, ycoord, towerClass, playerId));
    }
}
