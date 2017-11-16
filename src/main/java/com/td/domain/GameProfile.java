package com.td.domain;


import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "user_profile")
public class GameProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long level;

    @Column(name = "gameclass")
    private String gameClass;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public GameProfile() {
        this.level = 0L;
        this.gameClass = "Adventurer";
    }

    public GameProfile(@NotNull String gameClass) {
        this.gameClass = gameClass;
        this.level = 0L;
    }

    public String getGameClass() {
        return gameClass;
    }

    public Long getLevel() {
        return level;
    }

    public void setUser(User owner) {
        this.user = owner;
    }
}
