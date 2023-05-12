package com.cisco.workspaceintegrations.common.integration.features;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.cisco.workspaceintegrations.common.ValueObject;

import static com.google.common.base.Preconditions.checkNotNull;

public class CrossLaunchResponse extends ValueObject {

    private final URI redirectUrl;

    @JsonCreator
    public CrossLaunchResponse(@JsonProperty(value = "redirectUrl", required = true) URI redirectUrl) {
        this.redirectUrl = checkNotNull(redirectUrl);
    }

    public URI getRedirectUrl() {
        return redirectUrl;
    }
}
