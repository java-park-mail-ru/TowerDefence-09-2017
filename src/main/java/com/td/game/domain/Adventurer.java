package com.td.game.domain;

import com.td.game.gameObjects.Tower;

import java.util.List;

public class Adventurer extends PlayerClass{
    Adventurer(List<Tower> availableTowers) {
        super(availableTowers);
    }
}
