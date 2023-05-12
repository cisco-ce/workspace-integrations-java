package com.cisco.workspaceintegrations.common.actions;

public class HealthCheckRequest extends Action {

    public HealthCheckRequest(String orgId, String appId) {
        super(orgId, appId);
    }

    @Override
    public String toString() {
        return "Health Check Request: orgId=" + getOrgId() + ", appId=" + getAppId();
    }
}
