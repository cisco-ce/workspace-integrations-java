package com.cisco.workspaceintegrations.common.xapi;

import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;

import com.cisco.workspaceintegrations.common.ValueObject;
import com.cisco.workspaceintegrations.common.json.Json;
import com.cisco.workspaceintegrations.common.json.Json.MatchingNode;

import static com.google.common.base.Preconditions.checkNotNull;

public final class XAPIStatus extends ValueObject {

    private final JsonNode statusJson;

    public XAPIStatus(JsonNode statusJson) {
        this.statusJson = checkNotNull(statusJson);
    }

    public JsonNode getStatusJson() {
        return statusJson;
    }

    public Optional<MatchingNode> getNode(String... elements) {
        return Json.getMatchingNode(statusJson, elements).stream().findFirst();
    }
}
