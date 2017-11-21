package com.td.game.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Adventurer extends PlayerClass {
    @JsonCreator
    Adventurer(@JsonProperty("availableTowers") List<Integer> availableTowers) {
        super(availableTowers);
    }

    @Override
    public String toString() {
        return "Adventurer";
    }
}
