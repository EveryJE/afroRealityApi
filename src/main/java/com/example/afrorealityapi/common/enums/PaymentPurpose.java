package com.example.afrorealityapi.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentPurpose {
    TICKET_PURCHASE("ticket_purchase"),
    VOTE_PURCHASE("vote_purchase"),
    NOMINATION("nomination"),
    WALLET_TOPUP("wallet_topup");

    private final String value;

    PaymentPurpose(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
