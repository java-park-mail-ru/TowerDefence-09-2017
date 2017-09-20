package com.td.models;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseStatus {

    @JsonProperty
    private final String status;

    @JsonCreator
    public ResponseStatus(@JsonProperty("response-status") String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
