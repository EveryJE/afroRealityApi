package com.example.afrorealityapi.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CommissionType {
    SIGNUP("signup"),
    TICKET_PURCHASE("ticket_purchase"),
    VOTE_PURCHASE("vote_purchase"),
    SUBSCRIPTION("subscription"),
    BONUS("bonus");

    private final String value;

    CommissionType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
