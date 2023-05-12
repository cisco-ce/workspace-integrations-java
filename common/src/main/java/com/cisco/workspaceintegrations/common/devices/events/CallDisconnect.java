package com.cisco.workspaceintegrations.common.devices.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.cisco.workspaceintegrations.common.ValueObject;

public final class CallDisconnect extends ValueObject {
    private final String callId;
    private final int durationInSeconds;

    @JsonCreator
    private CallDisconnect(@JsonProperty("CallId") String callId, @JsonProperty("Duration") int durationInSeconds) {
        this.callId = callId;
        this.durationInSeconds = durationInSeconds;
    }

    public String getCallId() {
        return callId;
    }

    public int getDurationInSeconds() {
        return durationInSeconds;
    }
}
