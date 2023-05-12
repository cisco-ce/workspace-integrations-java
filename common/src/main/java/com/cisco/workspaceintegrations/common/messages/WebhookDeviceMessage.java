package com.cisco.workspaceintegrations.common.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class WebhookDeviceMessage extends WebhookMessage {

    private final String deviceId;
    private final String workspaceId;
    private final String orgId;
    private final String appId;

    public WebhookDeviceMessage(@JsonProperty("deviceId") String deviceId,
                                @JsonProperty("workspaceId") String workspaceId,
                                @JsonProperty("orgId") String orgId,
                                @JsonProperty("appId") String appId) {
        this.deviceId = checkNotNull(deviceId);
        this.workspaceId = checkNotNull(workspaceId);
        this.orgId = checkNotNull(orgId);
        this.appId = appId;
    }

    @JsonProperty("deviceId")
    public String deviceId() {
        return deviceId;
    }

    @JsonProperty("workspaceId")
    public String workspaceId() {
        return workspaceId;
    }

    @Override
    @JsonProperty("orgId")
    public String orgId() {
        return orgId;
    }

    @Override
    @JsonProperty("appId")
    public String appId() {
        return appId;
    }
}
