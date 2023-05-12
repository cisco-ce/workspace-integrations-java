package com.cisco.workspaceintegrations.common.actions;

import com.cisco.workspaceintegrations.common.ValueObject;

public class Action extends ValueObject {

    public static final String ACTION_PROVISION = "provision";
    public static final String ACTION_DEPROVISION = "deprovision";
    public static final String ACTION_HEALTH_CHECK = "healthCheck";
    public static final String ACTION_UPDATE_APPROVED = "updateApproved";
    public static final String ACTION_UPDATE = "update";

    private final String orgId;
    private final String appId;

    public Action(String orgId, String appId) {
        this.orgId = orgId;
        this.appId = appId;

    }

    public String getOrgId() {
        return orgId;
    }

    public String getAppId() {
        return appId;
    }
}
