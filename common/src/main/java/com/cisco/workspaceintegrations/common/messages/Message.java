package com.cisco.workspaceintegrations.common.messages;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import com.cisco.workspaceintegrations.common.ValueObject;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = WebhookStatusMessage.class, name = WebhookMessage.WEBHOOK_STATUS_MESSAGE_TYPE),
    @JsonSubTypes.Type(value = WebhookEventsMessage.class, name = WebhookMessage.WEBHOOK_EVENTS_MESSAGE_TYPE),
    @JsonSubTypes.Type(value = WebhookHealthCheckMessage.class, name = WebhookMessage.WEBHOOK_HEALTH_CHECK_MESSAGE_TYPE),
    @JsonSubTypes.Type(value = WebhookOnlyWebhookHealthCheckMessage.class, name = WebhookMessage.WEBHOOK_HEALTH_CHECK_MESSAGE_TYPE),
    @JsonSubTypes.Type(value = ActionMessage.class, name = ActionMessage.MESSAGE_TYPE)
})
public abstract class Message extends ValueObject {

    public abstract String type();
}
