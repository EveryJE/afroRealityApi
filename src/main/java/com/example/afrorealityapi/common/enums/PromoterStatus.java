package com.example.afrorealityapi.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PromoterStatus {
    PENDING("pending"),
    ACTIVE("active"),
    SUSPENDED("suspended"),
    INACTIVE("inactive");

    private final String value;

    PromoterStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
