package com.td.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
public class Progress implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "achievement_id", referencedColumnName = "id")
    private Achievement achievement;

    @Column(nullable = false)
    private BigDecimal completed;


}
