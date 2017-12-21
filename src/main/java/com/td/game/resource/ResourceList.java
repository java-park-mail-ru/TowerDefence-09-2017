package com.td.game.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ResourceList {
    private List<String> resourcesUrls;

    ResourceList(@JsonProperty("resources") List<String> resourcesUrls) {
        this.resourcesUrls = resourcesUrls;
    }

    public List<String> getResourcesUrls() {
        return resourcesUrls;
    }
}
