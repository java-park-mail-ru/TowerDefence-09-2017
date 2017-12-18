package com.td.game;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Clock;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class GameExecutor implements Runnable {

    private static final long STEP_TIME = 50;
    private final Logger logger = LoggerFactory.getLogger(GameExecutor.class);

    @NotNull
    private final GameManager gameManager;
    private final GameContext context;

    private Clock clock = Clock.systemDefaultZone();

    public GameExecutor(@NotNull GameManager gameManager, GameContext context) {
        this.gameManager = gameManager;
        this.context=  context;
    }

    @Override
    public void run() {
        gameCycle();
    }

    public void gameCycle() {
        long lastTickTime = STEP_TIME;
        context.setTimeBuffer(STEP_TIME);

        while (true) {
            long before = clock.millis();
            gameManager.gameStep(lastTickTime, context);

            long after = clock.millis();

            final long sleepingTime = Math.max(0, STEP_TIME - (after - before));
            try {
                Thread.sleep(sleepingTime);
            } catch (InterruptedException e) {
                logger.error("Thread was interrupted: ", e);
            }
            long afterSleep = clock.millis();
            lastTickTime += afterSleep - before;
            if(afterSleep < before) {
                logger.warn("LOL: {}, {}", afterSleep, before);
            }
        }
    }
}
