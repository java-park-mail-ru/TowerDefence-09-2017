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
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class GameScheduler {

    private final int concurrencyLevel = Runtime.getRuntime().availableProcessors();

    private final Queue<GameContext> gameContexts;

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
        Comparator<GameContext> comp = (lhs, rhs) -> {
            int sc = Integer.compare(lhs.getSessionsSize(), rhs.getSessionsSize());
            if (sc == 0) {
                return Integer.compare(lhs.getWaitersQueueLength(), rhs.getWaitersQueueLength());
            }
            return sc;
        };

        gameContexts = new PriorityQueue<>(comp);
        executorService = Executors.newFixedThreadPool(concurrencyLevel);
    }

    @PostConstruct
    void runExecutors() {
        logger.info("Game executros number: {}", concurrencyLevel);
        for (int i = 0; i < concurrencyLevel; ++i) {
            GameContext context = new GameContext();
            gameContexts.offer(context);
            executorService.submit(new GameExecutor(gameManager, context));
        }
    }

    public void addWaiter(Long id) {
        synchronized (gameContexts) {
            logger.debug("Waiter {} comes to queue", id);
            GameContext context = gameContexts.poll();
            context.addWaiter(id);
            gameContexts.offer(context);
        }
    }

    public void addTowerOrder(long xcoord, long ycoord, Integer towerClass, Long playerId) {
        GameContext context = gameContextService.getContext(playerId);
        context.addTowerOrder(new TowerManager.TowerOrder(xcoord, ycoord, towerClass, playerId));
    }

    public Queue<GameContext> getGameContexts() {
        return gameContexts;
    }
}
