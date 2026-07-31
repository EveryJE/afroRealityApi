package com.example.afrorealityapi.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TicketCheckInStatus {
    NOT_CHECKED_IN("not_checked_in"),
    CHECKED_IN("checked_in");

    private final String value;

    TicketCheckInStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
