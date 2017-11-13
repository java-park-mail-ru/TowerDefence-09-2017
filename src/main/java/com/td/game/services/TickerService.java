package com.td.game.services;

import org.springframework.stereotype.Service;

@Service
public class TickerService {
    private long ms;

    public void tick(long delta) {
        ms += delta;
    }

    public long getMs() {
        return ms;
    }

    public void reset() {
        ms = 0;
    }
}
