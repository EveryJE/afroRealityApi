package com.example.afrorealityapi.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CommissionStatus {
    PENDING("pending"),
    APPROVED("approved"),
    PAID("paid"),
    REJECTED("rejected"),
    CANCELLED("cancelled");

    private final String value;

    CommissionStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
