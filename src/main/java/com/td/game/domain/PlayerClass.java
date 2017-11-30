package com.td.game.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.td.game.resource.Resource;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(Adventurer.class),
})
public abstract class PlayerClass extends Resource {
    private List<Integer> availableTowers;

    @JsonCreator
    PlayerClass(List<Integer> availableTowers) {
        this.availableTowers = availableTowers;
    }

    public List<Integer> getAvailableTowers() {
        return availableTowers;
    }

    @JsonProperty("className")
    public String getClassName() {
        return toString();
    }

}
