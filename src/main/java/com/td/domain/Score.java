package com.td.domain;


import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "scores")
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private int score;

    private OffsetDateTime scoreDate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;

    public Score(int score, User owner) {
        this.id = 0L;
        this.score = score;
        this.owner = owner;
        this.scoreDate = OffsetDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public OffsetDateTime getScoreDate() {
        return scoreDate;
    }

    public void setScoreDate(OffsetDateTime scoreDate) {
        this.scoreDate = scoreDate;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
