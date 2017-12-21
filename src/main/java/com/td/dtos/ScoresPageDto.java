package com.td.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.td.domain.Score;

import java.util.List;
import java.util.stream.Collectors;

public class ScoresPageDto {
    private List<ScoreDto> scores;

    public ScoresPageDto(List<Score> rawScores) {
        this.scores = rawScores.stream()
                .map(ScoreDto::new)
                .collect(Collectors.toList());
    }

    @JsonProperty("scores")
    public List<ScoreDto> getScores() {
        return scores;
    }
}
