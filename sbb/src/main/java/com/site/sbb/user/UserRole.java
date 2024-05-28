package com.site.sbb.user;

import lombok.*;

@Getter

public enum UserRole {
    ADMIN("ROLE_AMDIN"),
    USER("ROLE_USER");

    UserRole(String value) {
        this.value = value;
    }

    private String value;
}
