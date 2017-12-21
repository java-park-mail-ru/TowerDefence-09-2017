package com.td.game.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.td.game.resource.Resource;


public class GameParams extends Resource {
    private final int baseMaxHp;
    private final long initialMoney;

    GameParams(@JsonProperty("maxHp") int baseMaxHp,
               @JsonProperty("startMoney") long initialMoney) {
        this.baseMaxHp = baseMaxHp;
        this.initialMoney = initialMoney;
    }

    public int getBaseMaxHp() {
        return baseMaxHp;
    }

    public long getInitialMoney() {
        return initialMoney;
    }


}
