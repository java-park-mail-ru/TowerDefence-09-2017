package com.td.game.resource;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.td.game.domain.GameMap;
import com.td.game.gameobjects.Monster;
import com.td.game.gameobjects.Path;
import com.td.game.gameobjects.Tower;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(Monster.MonsterResource.class),
        @JsonSubTypes.Type(Tower.TowerResource.class),
        @JsonSubTypes.Type(GameMap.GameMapResource.class),
        @JsonSubTypes.Type(Path.PathResource.class),
})
public class Resource {
}
