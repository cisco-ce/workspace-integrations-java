package com.cisco.workspaceintegrations.common.integration.features;

import java.net.URI;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.cisco.workspaceintegrations.common.ValueObject;

import static com.google.common.base.Preconditions.checkNotNull;

public class CrossLaunchDevice extends ValueObject {

    private final URI deviceUrl;
    private final String displayName;

    @JsonCreator
    public CrossLaunchDevice(@JsonProperty(value = "deviceUrl", required = true) URI deviceUrl,
                             @JsonProperty(value = "displayName") String displayName) {
        this.deviceUrl = checkNotNull(deviceUrl);
        this.displayName = displayName;
    }

    public URI getDeviceUrl() {
        return deviceUrl;
    }

    public Optional<String> getDisplayName() {
        return Optional.ofNullable(displayName);
    }
}
