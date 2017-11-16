package com.td.game.services;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Clock;

@Service
public class TickerService {
    private long ms;

    @PostConstruct
    public void init() {
        ms = Clock.systemDefaultZone().millis();
    }

    public void update(long current) {
        ms = current;
    }

    public long getDelta(long current) {
        return Math.abs(current - ms);
    }

    public long getMs() {
        return ms;
    }

    public void reset() {
        ms = 0;
    }
}
