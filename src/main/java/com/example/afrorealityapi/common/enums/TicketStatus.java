package com.example.afrorealityapi.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TicketStatus {
    AVAILABLE("available"),
    SOLD_OUT("sold_out"),
    HIDDEN("hidden"),
    EXPIRED("expired");

    private final String value;

    TicketStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
