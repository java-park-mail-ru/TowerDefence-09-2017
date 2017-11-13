package com.td.game.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.td.game.gameObjects.Tower;
import com.td.game.resourceSystem.Resource;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(Adventurer.class),
})
public abstract class PlayerClass extends Resource {
    private List<Tower> availableTowers;

    @JsonCreator
    PlayerClass(List<Tower> availableTowers) {
        this.availableTowers = availableTowers;
    }

    public List<Tower> getAvailableTowers() {
        return availableTowers;
    }
}
