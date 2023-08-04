package com.cisco.workspaceintegrations.common.actions;

import java.time.Instant;

public class JwtExpiredException extends RuntimeException {
    private final Instant expiryTime;

    public JwtExpiredException(String message, Instant expiryTime) {
        super(message);
        this.expiryTime = expiryTime;
    }

    public Instant getExpiryTime() {
        return expiryTime;
    }
}
