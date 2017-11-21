package com.td.game;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Clock;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
public class GameExecutor implements Runnable {

    private static final long STEP_TIME = 50;

    @NotNull
    private final Game game;

    private Clock clock = Clock.systemDefaultZone();

    private Executor executor = Executors.newSingleThreadExecutor();

    public GameExecutor(@NotNull Game game) {
        this.game = game;
    }

    @PostConstruct
    public void init() {
        executor.execute(this);
    }

    @Override
    public void run() {
        gameCycle();
    }

    public void gameCycle() {
        long lastTickTime = STEP_TIME;
        while (true) {
            long before = clock.millis();
            game.gameStep(lastTickTime);
            long after = clock.millis();

            final long sleepingTime = Math.max(0, STEP_TIME - (after - before));
            try {
                Thread.sleep(sleepingTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long afterSleep = clock.millis();
            lastTickTime += afterSleep - before;
        }
    }
}
