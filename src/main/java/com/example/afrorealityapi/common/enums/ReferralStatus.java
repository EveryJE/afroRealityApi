package com.example.afrorealityapi.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ReferralStatus {
    PENDING("pending"),
    VERIFIED("verified"),
    CONVERTED("converted"),
    EXPIRED("expired");

    private final String value;

    ReferralStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
