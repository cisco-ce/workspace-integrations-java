package com.cisco.workspaceintegrations.common.xapi;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import com.cisco.workspaceintegrations.common.ValueObject;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

public final class CommandResponse extends ValueObject {

    private final String deviceId;
    private final JsonNode result;
    private final Map<String, JsonNode> arguments;

    @JsonCreator
    public CommandResponse(@JsonProperty("deviceId") String deviceId,
                           @JsonProperty("arguments") Map<String, JsonNode> arguments,
                           @JsonProperty("result") JsonNode result) {
        this.deviceId = deviceId;
        this.arguments = arguments != null ? unmodifiableMap(arguments) : emptyMap();
        this.result = result;
    }

    public String deviceId() {
        return deviceId;
    }

    public Map<String, JsonNode> arguments() {
        return arguments;
    }

    public JsonNode result() {
        return result;
    }
}
