package com.td.game.domain;

import com.td.game.gameObjects.Monster;

import java.util.ArrayDeque;
import java.util.Deque;

public class Area {
    private Point<Long> top;
    private Point<Long> bottom;
    private Deque<Monster> monsters = new ArrayDeque<>();


    public Area(long xtop, long ytop, long xbot, long ybot) {
        this.top = new Point<>(xtop, ytop);
        this.bottom = new Point<>(xbot, ybot);
    }

    public boolean isEmpty() {
        return monsters.isEmpty();
    }

    public Monster peekFirst() {
        return monsters.peekFirst();
    }

    public void removeFirst() {
        monsters.pollFirst();
    }

    public void addObject(Monster obj) {
        monsters.offer(obj);
    }

    public void removeObject(Long id) {
        monsters.removeIf(monster -> monster.getId().equals(id));
    }

    public Point<Long> getBottom() {
        return bottom;
    }

    public void setBottom(Point<Long> bottom) {
        this.bottom = bottom;
    }

    public Point<Long> getTop() {
        return top;
    }

    public void setTop(Point<Long> top) {
        this.top = top;
    }
}
