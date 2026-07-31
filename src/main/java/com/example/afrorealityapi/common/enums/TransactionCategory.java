package com.example.afrorealityapi.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TransactionCategory {
    TICKET_PURCHASE("ticket_purchase"),
    VOTE_PURCHASE("vote_purchase"),
    SUBSCRIPTION("subscription"),
    REFUND("refund"),
    COMMISSION_PAYOUT("commission_payout"),
    WALLET_TOPUP("wallet_topup"),
    WALLET_WITHDRAWAL("wallet_withdrawal"),
    TRANSFER("transfer"),
    FEE("fee"),
    BONUS("bonus"),
    ADJUSTMENT("adjustment");

    private final String value;

    TransactionCategory(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
