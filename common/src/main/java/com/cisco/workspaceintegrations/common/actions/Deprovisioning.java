package com.cisco.workspaceintegrations.common.actions;

public class Deprovisioning extends Action {

    public Deprovisioning(String orgId, String appId) {
        super(orgId, appId);
    }

    @Override
    public String toString() {
        return "Deprovisioning: orgId=" + getOrgId() + ", appId=" + getAppId();
    }
}
