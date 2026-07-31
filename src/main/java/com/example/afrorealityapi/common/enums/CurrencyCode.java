package com.example.afrorealityapi.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;



public enum CurrencyCode {
    NGN("NGN"),
    USD("USD"),
    GHS("GHS"),
    KES("KES"),
    ZAR("ZAR"),
    GBP("GBP"),
    EUR("EUR");

    private final String value;

    CurrencyCode(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
