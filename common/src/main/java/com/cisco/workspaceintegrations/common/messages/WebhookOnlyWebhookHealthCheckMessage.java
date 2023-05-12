package com.cisco.workspaceintegrations.common.messages;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.google.common.base.Preconditions.checkNotNull;

public class WebhookOnlyWebhookHealthCheckMessage extends WebhookMessage {

    private final String orgId;
    private final Instant timestamp;

    @JsonCreator
    public WebhookOnlyWebhookHealthCheckMessage(
        @JsonProperty("orgId") String orgId,
        @JsonProperty("timestamp") Instant timestamp) {
        this.orgId = orgId;
        this.timestamp = checkNotNull(timestamp);
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

    @Override
    public String appId() {
        return null;
    }
}
