package com.td.controllers;

import com.td.daos.ScoresDao;
import com.td.domain.Score;
import com.td.dtos.ScoresPageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/scores")
public class ScoresController {
    private final ScoresDao scoresDao;

    private final int pageSize = 10;

    @Autowired
    public ScoresController(ScoresDao scoresDao) {
        this.scoresDao = scoresDao;
    }

    @GetMapping(value = {"/{mark}/page/{limit}", "/page/{limit}", "/{mark}", "/"}, produces = "application/json")
    @ResponseBody
    public ResponseEntity getScores(@PathVariable(value = "mark") Optional<Long> mark,
                                    @PathVariable(value = "limit") Optional<Integer> limit) {
        List<Score> scores;

        if (mark.isPresent() && limit.isPresent()) {
            scores = scoresDao.getScoresList(mark.get(), limit.get());
        } else if (limit.isPresent()) {
            scores = scoresDao.getScoresList(limit.get());
        } else if (mark.isPresent()) {
            scores = scoresDao.getScoresList(mark.get(), pageSize);
        } else {
            scores = scoresDao.getScoresList(pageSize);
        }

        return ResponseEntity.ok(new ScoresPageDto(scores));
    }
}
