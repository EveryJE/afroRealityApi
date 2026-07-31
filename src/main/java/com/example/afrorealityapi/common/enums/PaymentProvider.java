package com.example.afrorealityapi.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentProvider {
    PAYSTACK("paystack"),
    FLUTTERWAVE("flutterwave"),
    STRIPE("stripe"),
    BANK_TRANSFER("bank_transfer"),
    WALLET("wallet"),
    CASH("cash"),
    FREE("free");

    private final String value;

    PaymentProvider(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
