package com.td.game;

import com.td.game.services.TowerManager;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameContext {

    private final Set<GameSession> sessions;
    private final Queue<Long> waiters;
    private final Queue<TowerManager.TowerOrder> towerOrders;
    private long timeBuffer;


    public GameContext() {
        this.towerOrders = new ConcurrentLinkedQueue<>();
        this.sessions = new HashSet<>();
        this.waiters = new ConcurrentLinkedQueue<>();
        this.timeBuffer = 0L;
    }

    public void addSession(GameSession session) {
        sessions.add(session);
    }

    public void addWaiter(Long id) {
        waiters.add(id);
    }

    public void addTowerOrder(TowerManager.TowerOrder towerOrder) {
        towerOrders.add(towerOrder);
    }

    public Set<GameSession> getSessions() {
        return this.sessions;
    }

    public Queue<Long> getWaiters() {
        return this.waiters;
    }

    public Queue<TowerManager.TowerOrder> getTowerOrders() {
        return towerOrders;
    }

    public void removeSession(@NotNull GameSession session) {
        sessions.remove(session);
    }

    public long updateTimeBuffer(long time) {
        long delta = time - timeBuffer;
        timeBuffer = time;
        return delta;
    }

    public void setTimeBuffer(long timeBuffer) {
        this.timeBuffer = timeBuffer;
    }

    public int getSessionsSize() {
        return this.sessions.size();
    }

    public int getWaitersQueueLength(){
        return this.waiters.size();
    }
}
