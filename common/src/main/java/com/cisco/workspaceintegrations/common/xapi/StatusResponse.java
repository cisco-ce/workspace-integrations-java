package com.cisco.workspaceintegrations.common.xapi;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import com.cisco.workspaceintegrations.common.ValueObject;

import static com.google.common.base.Preconditions.checkNotNull;

public final class StatusResponse extends ValueObject {

    private final String deviceId;
    private final JsonNode result;

    @JsonCreator
    public StatusResponse(@JsonProperty("deviceId") String deviceId,
                          @JsonProperty("result") JsonNode result) {
        this.deviceId = checkNotNull(deviceId);
        this.result = checkNotNull(result);
    }

    public String deviceId() {
        return deviceId;
    }


    public XAPIStatus result() {
        return new XAPIStatus(result);
    }
}
