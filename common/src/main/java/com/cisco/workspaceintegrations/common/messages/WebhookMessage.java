package com.cisco.workspaceintegrations.common.messages;

public abstract class WebhookMessage extends Message {

    public static final String WEBHOOK_STATUS_MESSAGE_TYPE = "status";
    public static final String WEBHOOK_EVENTS_MESSAGE_TYPE = "events";
    public static final String WEBHOOK_HEALTH_CHECK_MESSAGE_TYPE = "healthCheck";

    public abstract String orgId();

    public abstract String appId();
}
