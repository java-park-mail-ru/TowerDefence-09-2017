package com.td.game.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResourceFactory {

    private final ObjectMapper objectMapper;
    private ResourceReg resourceReg;

    @Autowired
    public ResourceFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() throws IOException {
        this.resourceReg = objectMapper.readValue(Resources.getResource("ResourceReg.json"), ResourceReg.class);
    }

    public Resource getRawResource(String path) {
        return loadResource(path, Resource.class);
    }

    public <T extends Resource> T loadResource(String path, Class<T> clazz) {
        T resource;
        try {
            resource = objectMapper.readValue(Resources.getResource(path), clazz);
        } catch (IOException e) {
            throw new ResourceException(clazz, path, e);
        }
        return resource;
    }

    public <T extends Resource> T loadResource(Integer typeid, Class<T> clazz) {
        return loadResource(resourceReg.getResourcePath(typeid), clazz);
    }

    public <T extends Resource> List<T> loadResourceList(String path, Class<T> clazz) {

        try {
            ResourceList resList = objectMapper.readValue(Resources.getResource(path), ResourceList.class);
            return resList
                    .getResourcesUrls()
                    .stream()
                    .map(url -> loadResource(url, clazz))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new ResourceException(clazz, path, e);
        }
    }

}
