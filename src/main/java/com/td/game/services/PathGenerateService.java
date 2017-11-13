package com.td.game.services;

import com.td.game.domain.PathPoint;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PathGenerateService {

    public List<PathPoint> generatePath() {
        List<PathPoint> path = new ArrayList<>();
        path.add(new PathPoint(0, 1, 1, 1, 0));
        path.add(new PathPoint(1, 1, 1, 1, 0));
        path.add(new PathPoint(2, 1, 1, 1, 0));
        path.add(new PathPoint(3, 1, 1, 1, 0));
        path.add(new PathPoint(4, 1, 1, 1, 0));
        path.add(new PathPoint(5, 1, 1, 1, 0));

        path.add(new PathPoint(6, 1, 1, 0, 1));
        path.add(new PathPoint(6, 2, 1, 0, 1));
        path.add(new PathPoint(6, 3, 1, 0, 1));
        path.add(new PathPoint(6, 4, 1, 0, 1));

        path.add(new PathPoint(6, 5, 1, -1, 0));
        path.add(new PathPoint(5, 5, 1, -1, 0));
        path.add(new PathPoint(4, 5, 1, -1, 0));
        path.add(new PathPoint(3, 5, 1, -1, 0));


        path.add(new PathPoint(2, 5, 1, 0, 1));
        path.add(new PathPoint(2, 6, 1, 0, 1));
        path.add(new PathPoint(2, 7, 1, 0, 1));

        path.add(new PathPoint(2, 8, 1, 1, 0));
        path.add(new PathPoint(3, 8, 1, 1, 0));
        path.add(new PathPoint(4, 8, 1, 1, 0));
        path.add(new PathPoint(5, 8, 1, 1, 0));
        path.add(new PathPoint(6, 8, 1, 1, 0));
        path.add(new PathPoint(7, 8, 1, 1, 0));
        path.add(new PathPoint(8, 8, 1, 1, 0));

        path.add(new PathPoint(9, 8, 1, 0, -1));
        path.add(new PathPoint(9, 7, 1, 0, -1));
        path.add(new PathPoint(9, 6, 1, 0, -1));
        path.add(new PathPoint(9, 5, 1, 0, -1));
        path.add(new PathPoint(9, 4, 1, 0, -1));
        path.add(new PathPoint(9, 3, 1, 0, -1));
        path.add(new PathPoint(9, 2, 1, 0, -1));

        path.add(new PathPoint(9, 8, 1, 1, 0));
        path.add(new PathPoint(10, 7, 1, 1, 0));
        path.add(new PathPoint(11, 6, 1, 1, 0));
        path.add(new PathPoint(12, 5, 1, 1, 0));
        path.add(new PathPoint(13, 4, 1, 1, 0));
        path.add(new PathPoint(14, 3, 1, 1, 0));
        path.add(new PathPoint(15, 2, 1, 1, 0));
        path.add(new PathPoint(16, 1, 1, 1, 0));

        path.add(new PathPoint(17, 1, 1, 0, 1));
        path.add(new PathPoint(17, 2, 1, 0, 1));
        path.add(new PathPoint(17, 3, 1, 0, 1));
        path.add(new PathPoint(17, 4, 1, 0, 1));
        path.add(new PathPoint(17, 5, 1, 0, 1));
        path.add(new PathPoint(17, 6, 1, 0, 1));
        path.add(new PathPoint(17, 7, 1, 0, 1));
        path.add(new PathPoint(17, 8, 1, 0, 1));
        path.add(new PathPoint(17, 9, 1, 0, 1));
        path.add(new PathPoint(17, 10, 1, 0, 1));
        path.add(new PathPoint(17, 11, 1, 0, 1));

        path.add(new PathPoint(17, 12, 1, 1, 0));
        path.add(new PathPoint(17, 12, 1, 1, 0));
        path.add(new PathPoint(17, 12, 1, 1, 0));
        return path;

    }
}
