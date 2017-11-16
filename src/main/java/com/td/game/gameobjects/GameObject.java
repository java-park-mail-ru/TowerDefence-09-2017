package com.td.game.gameobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicLong;

public abstract class GameObject {

    private static final AtomicLong ID_SOURCE = new AtomicLong(0);

    @JsonProperty("id")
    @NotNull
    private final Long id;

    GameObject() {
        id = ID_SOURCE.getAndIncrement();
    }


    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        GameObject that = (GameObject) obj;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
