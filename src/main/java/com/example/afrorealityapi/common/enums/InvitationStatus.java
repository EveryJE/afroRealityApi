package com.example.afrorealityapi.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum InvitationStatus {
    PENDING("pending"),
    ACCEPTED("accepted"),
    DECLINED("declined"),
    EXPIRED("expired");

    private final String value;

    InvitationStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
