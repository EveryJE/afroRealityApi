package com.example.afrorealityapi.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EventStatus {
    DRAFT("draft"),
    PUBLISHED("published"),
    ONGOING("ongoing"),
    ENDED("ended"),
    CANCELLED("cancelled");

    private final String value;

    EventStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
