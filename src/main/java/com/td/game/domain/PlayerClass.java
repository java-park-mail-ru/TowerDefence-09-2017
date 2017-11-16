package com.td.game.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.td.game.resource.Resource;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(Adventurer.class),
})
public abstract class PlayerClass extends Resource {
    private List<String> availableTowers;

    @JsonCreator
    PlayerClass(List<String> availableTowers) {
        this.availableTowers = availableTowers;
    }

    public List<String> getAvailableTowers() {
        return availableTowers;
    }

}
