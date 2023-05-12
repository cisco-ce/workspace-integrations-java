package com.cisco.workspaceintegrations.common.oauth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.cisco.workspaceintegrations.common.ValueObject;

import static java.util.Objects.requireNonNull;

public final class OAuthClient extends ValueObject {
    private final String clientId;
    private final String clientSecret;

    @JsonCreator
    public OAuthClient(@JsonProperty(value = "clientId", required = true) String clientId,
                       @JsonProperty(value = "clientSecret", required = true) String clientSecret) {
        this.clientId = requireNonNull(clientId);
        this.clientSecret = requireNonNull(clientSecret);
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    @Override
    public String toString() {
        return "{clientId=" + clientId + "}";
    }
}
