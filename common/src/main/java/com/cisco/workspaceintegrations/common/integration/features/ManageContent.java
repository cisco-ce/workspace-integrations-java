package com.cisco.workspaceintegrations.common.integration.features;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.cisco.workspaceintegrations.common.ValueObject;

import static com.google.common.base.Preconditions.checkNotNull;

public class ManageContent extends ValueObject {

    private final URI url;

    @JsonCreator
    public ManageContent(@JsonProperty(value = "url", required = true) URI manageContent) {
        this.url = checkNotNull(manageContent);
    }

    public URI getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return url.toString();
    }
}
