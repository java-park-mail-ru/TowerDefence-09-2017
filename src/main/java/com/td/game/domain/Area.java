package com.td.game.domain;

import com.td.game.gameobjects.Monster;
import com.td.game.gameobjects.Tower;

import java.util.ArrayDeque;
import java.util.Deque;

public class Area {
    private Point<Long> top;
    private Point<Long> bottom;
    private Deque<Monster> monsters = new ArrayDeque<>();
    private Tower tower;

    public Area(long xtop, long ytop, long xbot, long ybot, Tower tower) {
        this.top = new Point<>(xtop, ytop);
        this.bottom = new Point<>(xbot, ybot);
        this.tower = tower;
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

    public boolean isUnderTopPoint(Point<Long> point) {
        return top.getXcoord() <= point.getXcoord()
                && top.getYcoord() <= point.getYcoord()
                && bottom.getXcoord() > point.getXcoord()
                && bottom.getYcoord() > point.getYcoord();
    }

    public boolean isOverBotPoint(Point<Long> point) {
        return top.getXcoord() < point.getXcoord()
                && top.getYcoord() < point.getYcoord()
                && bottom.getXcoord() >= point.getXcoord()
                && bottom.getYcoord() >= point.getYcoord();
    }

    public boolean checkCollision(Monster obj) {
        Point<Long> topCoord = obj.getCoord().getTileCoord();
        Point<Long> botCoord = new Point<>(topCoord.getXcoord() + 1, topCoord.getYcoord() + 1);
        return isUnderTopPoint(topCoord) || isOverBotPoint(botCoord);
    }

    public void addObjectIfCollision(Monster obj) {
        boolean check = checkCollision(obj);
        if (check) {
            monsters.offer(obj);
        }
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

    public void clear() {
        monsters.clear();
    }

    public Tower getTower() {
        return tower;
    }

    public int size() {
        return monsters.size();
    }
}
