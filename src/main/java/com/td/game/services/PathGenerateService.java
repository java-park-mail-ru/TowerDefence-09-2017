package com.td.game.services;

import com.td.game.gameobjects.Path;
import com.td.game.resource.ResourceFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class PathGenerateService {

    @NotNull
    private final ResourceFactory resourceFactory;

    public PathGenerateService(@NotNull ResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
    }

    public Path generatePath() {
        Path.PathResource res = resourceFactory.loadResource("Path.json", Path.PathResource.class);
        return new Path(res);

    }
}
