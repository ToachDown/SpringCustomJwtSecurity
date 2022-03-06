package com.example.jwtcleanspring.model.enums;

public enum Role {
    ADMIN(1),
    OPERATOR(2),
    USER(3);

    private final int priority;

    Role(int i) {
        this.priority = i;
    }

    public int getPriority() {
        return priority;
    }
}
