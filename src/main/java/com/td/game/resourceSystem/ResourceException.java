package com.td.game.resourceSystem;

public class ResourceException extends RuntimeException {

    ResourceException(Class failedClass, String path, Throwable cause) {
        super("Faield to load "
                .concat(failedClass.getName())
                .concat(" at path ")
                .concat(path), cause);
    }
}
