package com.cisco.workspaceintegrations.common.messages;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.google.common.base.Preconditions.checkNotNull;

public class WebhookHealthCheckMessage extends WebhookMessage {

    private final String orgId;
    private final String appId;
    private final Instant timestamp;

    @JsonCreator
    public WebhookHealthCheckMessage(
        @JsonProperty("orgId") String orgId,
        @JsonProperty("appId") String appId,
        @JsonProperty("timestamp") Instant timestamp) {
        this.orgId = orgId;
        this.appId = appId;
        this.timestamp = checkNotNull(timestamp);
    }

    @Override
    public String appId() {
        return appId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public String type() {
        return WEBHOOK_HEALTH_CHECK_MESSAGE_TYPE;
    }

    @Override
    @JsonProperty("orgId")
    public String orgId() {
        return orgId;
    }
}
