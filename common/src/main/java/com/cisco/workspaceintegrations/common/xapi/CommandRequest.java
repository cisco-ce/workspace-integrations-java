package com.cisco.workspaceintegrations.common.xapi;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.TextNode;

import com.cisco.workspaceintegrations.common.ValueObject;

import static java.util.Collections.unmodifiableMap;

public final class CommandRequest extends ValueObject {

    private final String deviceId;
    private final Map<String, JsonNode> arguments;
    private final JsonNode body;

    public CommandRequest(@JsonProperty("deviceId") String deviceId) {
        this(deviceId, null, null);
    }

    public CommandRequest(@JsonProperty("deviceId") String deviceId,
                          @JsonProperty("arguments") Map<String, JsonNode> arguments) {
        this(deviceId, arguments, null);
    }

    public CommandRequest(@JsonProperty("deviceId") String deviceId,
                          @JsonProperty("body") JsonNode body) {
        this(deviceId, null, body);
    }

    @JsonCreator
    public CommandRequest(@JsonProperty("deviceId") String deviceId,
                          @JsonProperty("arguments") Map<String, JsonNode> arguments,
                          @JsonProperty("body") JsonNode body) {
        this.deviceId = deviceId;
        this.arguments = arguments != null ? unmodifiableMap(arguments) : null;
        this.body = body;
    }

    public String deviceId() {
        return deviceId;
    }

    public Optional<Map<String, JsonNode>> arguments() {
        return Optional.ofNullable(arguments);
    }

    public Optional<JsonNode> body() {
        return Optional.ofNullable(body);
    }

    public static Map<String, JsonNode> arguments(String k1, Object v1) {
        return arguments(Map.of(k1, v1));
    }

    public static Map<String, JsonNode> arguments(String k1, Object v1, String k2, Object v2) {
        return arguments(Map.of(k1, v1, k2, v2));
    }

    public static Map<String, JsonNode> arguments(String k1, Object v1, String k2, Object v2, String k3, Object v3) {
        return arguments(Map.of(k1, v1, k2, v2, k3, v3));
    }

    public static Map<String, JsonNode> arguments(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4) {
        return arguments(Map.of(k1, v1, k2, v2, k3, v3, k4, v4));
    }

    public static Map<String, JsonNode> arguments(Map<String, Object> args) {
        Map<String, JsonNode> arguments = new HashMap<>(args.size());
        args.forEach((key, value) -> {
            if (value instanceof Integer) {
                arguments.put(key, IntNode.valueOf((Integer) value));
            } else {
                arguments.put(key, TextNode.valueOf(value.toString()));
            }
        });
        return unmodifiableMap(arguments);
    }
}
