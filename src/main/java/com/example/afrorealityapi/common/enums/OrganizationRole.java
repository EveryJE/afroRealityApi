package com.example.afrorealityapi.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OrganizationRole {
    OWNER("owner"),
    ADMIN("admin"),
    MEMBER("member");

    private final String value;

    OrganizationRole(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
