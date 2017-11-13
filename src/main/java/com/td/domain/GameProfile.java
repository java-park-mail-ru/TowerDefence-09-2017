package com.td.domain;


import javax.persistence.*;

@Entity
@Table(name = "profiles")
public class GameProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long level;

    private String gameClass;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
