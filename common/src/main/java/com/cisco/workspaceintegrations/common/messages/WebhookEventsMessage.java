package com.cisco.workspaceintegrations.common.messages;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import com.cisco.workspaceintegrations.common.Utils;
import com.cisco.workspaceintegrations.common.ValueObject;
import com.cisco.workspaceintegrations.common.xapi.Key;

import static com.google.common.base.Preconditions.checkNotNull;

public class WebhookEventsMessage extends WebhookDeviceMessage {

    private final Instant timestamp;
    private final List<Event> events;

    @JsonCreator
    public WebhookEventsMessage(@JsonProperty("deviceId") String deviceId,
                                @JsonProperty("workspaceId") String workspaceId,
                                @JsonProperty("orgId") String orgId,
                                @JsonProperty("appId") String appId,
                                @JsonProperty("timestamp") Instant timestamp,
                                @JsonProperty("events") List<Event> events) {
        super(deviceId, workspaceId, orgId, appId);
        this.timestamp = checkNotNull(timestamp);
        this.events = Utils.toUnmodifiableList(events);
    }

    @JsonProperty("timestamp")
    public Instant timestamp() {
        return timestamp;
    }

    @JsonProperty("events")
    public List<Event> events() {
        return events;
    }

    @Override
    public String type() {
        return WEBHOOK_EVENTS_MESSAGE_TYPE;
    }

    public static class Event extends ValueObject {

        @JsonProperty("key")
        private final Key key;
        @JsonProperty("value")
        private final JsonNode value;
        @JsonProperty("timestamp")
        private final Instant timestamp;

        @JsonCreator
        public Event(@JsonProperty("key") Key key,
                     @JsonProperty("value") JsonNode value,
                     @JsonProperty("timestamp") Instant timestamp) {
            this.key = checkNotNull(key);
            this.value = checkNotNull(value);
            this.timestamp = timestamp;
        }

        public Key key() {
            return key;
        }

        public JsonNode valueAsJson() {
            return value;
        }

        public Instant timestamp() {
            return timestamp;
        }
    }
}
