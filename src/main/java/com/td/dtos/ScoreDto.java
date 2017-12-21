package com.td.dtos;

import com.td.domain.Score;

public class ScoreDto {
    private long scoreId;
    private String nickname;
    private int scores;

    public ScoreDto(Score score) {
        this.scoreId = score.getId();
        this.nickname = score.getOwner().getNickname();
        this.scores = score.getScore();
    }

    public long getScoreId() {
        return scoreId;
    }

    public void setScoreId(long scoreId) {
        this.scoreId = scoreId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getScores() {
        return scores;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }
}
