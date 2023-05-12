package com.cisco.workspaceintegrations.common.actions;

import java.util.Optional;

public class Updated extends Action {

    private final String refreshToken;

    public Updated(String orgId, String appId, String refreshToken) {
        super(orgId, appId);
        this.refreshToken = refreshToken;
    }

    public Optional<String> getRefreshToken() {
        return Optional.ofNullable(refreshToken);
    }

    @Override
    public String toString() {
        return "Updated: orgId=" + getOrgId() + ", appId=" + getAppId();
    }
}
