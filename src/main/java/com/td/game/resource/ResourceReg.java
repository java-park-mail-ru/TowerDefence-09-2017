package com.td.game.resource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceReg extends Resource {

    private final Map<Integer, String> reg;

    @JsonCreator
    public ResourceReg(@JsonProperty("reg") List<ResourceRow> rows) {
        this.reg = new HashMap<>();
        for (ResourceRow row : rows) {
            reg.put(row.getTypeid(), row.getResourcePath());
        }
    }

    public String getResourcePath(Integer typeid) {
        return reg.get(typeid);
    }


    public static class ResourceRow {
        private Integer typeid;
        private String resourcePath;

        @JsonCreator
        ResourceRow(@JsonProperty("typeid") Integer typeid,
                    @JsonProperty("resource") String resourcePath) {
            this.typeid = typeid;
            this.resourcePath = resourcePath;
        }

        public Integer getTypeid() {
            return typeid;
        }

        public String getResourcePath() {
            return resourcePath;
        }
    }
}
