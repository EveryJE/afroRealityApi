package com.example.afrorealityapi.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EventMemberStatus {
    INVITED("invited"),
    ATTENDED("attended"),
    VOTED("voted");

    private final String value;

    EventMemberStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
