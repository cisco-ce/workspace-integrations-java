package com.cisco.workspaceintegrations.common.integration.features;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.cisco.workspaceintegrations.common.ValueObject;

import static com.google.common.base.Preconditions.checkNotNull;

public class AssignContent extends ValueObject {

    private final URI url;

    @JsonCreator
    public AssignContent(@JsonProperty(value = "url", required = true) URI url) {
        this.url = checkNotNull(url);
    }

    public URI getUrl() {
        return url;
    }
}
