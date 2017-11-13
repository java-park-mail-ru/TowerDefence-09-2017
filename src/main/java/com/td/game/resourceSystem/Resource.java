package com.td.game.resourceSystem;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.td.game.gameObjects.Monster;
import com.td.game.gameObjects.Tower;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(Monster.MonsterResource.class),
        @JsonSubTypes.Type(Tower.TowerResource.class),
})
public class Resource {
}
