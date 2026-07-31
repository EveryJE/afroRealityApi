package com.example.afrorealityapi.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EventType {
    VOTING("voting"),
    TICKETED("ticketed"),
    STANDARD("standard"),
    HYBRID("hybrid");

    private final String value;

    EventType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
