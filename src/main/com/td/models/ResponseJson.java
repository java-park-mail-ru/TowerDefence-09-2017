package com.td.models;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseJson {

    private final String status;

    @JsonCreator
    public ResponseJson(@JsonProperty("status") String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
