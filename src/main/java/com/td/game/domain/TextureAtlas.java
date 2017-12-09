package com.td.game.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.td.game.resource.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TextureAtlas extends Resource {


    private final Map<Integer, DisplayInfo> atlas;

    @JsonCreator
    TextureAtlas(@JsonProperty("atlas") List<DisplayInfo> displayInfoList) {
        this.atlas = new HashMap<>();
        for (DisplayInfo info : displayInfoList) {
            atlas.put(info.getType(), info);
        }
    }

    public DisplayInfo getDisplayInfoFor(Integer type) {
        return atlas.get(type);
    }

    public Map<Integer, DisplayInfo> getAtlas() {
        return atlas;
    }

    public static class DisplayInfo {
        private Integer type;
        private String text;
        private String texture;
        private List<List<String>> texturesPacks;

        @JsonCreator
        public DisplayInfo(@JsonProperty("type") Integer type,
                           @JsonProperty("text") String text,
                           @JsonProperty("texture") String texture,
                           @JsonProperty("texturesPacks") List<List<String>> texturesPacks) {
            this.type = type;
            this.text = text;
            this.texture = texture;
            this.texturesPacks = texturesPacks;
        }

        public Integer getType() {
            return type;
        }

        public String getText() {
            return text;
        }

        public String getTexture() {
            return texture;
        }

        public List<List<String>> getTexturesPacks() {
            return texturesPacks;
        }
    }
}
