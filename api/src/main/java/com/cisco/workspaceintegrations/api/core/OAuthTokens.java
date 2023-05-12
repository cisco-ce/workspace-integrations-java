package com.cisco.workspaceintegrations.api.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OAuthTokens {

    @JsonProperty("access_token")
    private final String accessToken;
    @JsonProperty("expires_in")
    private final Long expiresIn;
    @JsonProperty("refresh_token")
    private final String refreshToken;
    @JsonProperty("refresh_token_expires_in")
    private final Integer refreshTokenExpiresIn;

    @JsonCreator
    public OAuthTokens(@JsonProperty("access_token") String accessToken,
                       @JsonProperty("expires_in") Long expiresIn,
                       @JsonProperty("refresh_token") String refreshToken,
                       @JsonProperty("refresh_token_expires_in") Integer refreshTokenExpiresIn) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
    }

    public String accessToken() {
        return this.accessToken;
    }

    public Long expiresIn() {
        return this.expiresIn;
    }

    public String refreshToken() {
        return this.refreshToken;
    }

    public int refreshTokenExpiresIn() {
        return this.refreshTokenExpiresIn != null ? this.refreshTokenExpiresIn : 0;
    }

}
