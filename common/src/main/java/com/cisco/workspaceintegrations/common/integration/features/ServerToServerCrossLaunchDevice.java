package com.cisco.workspaceintegrations.common.integration.features;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.cisco.workspaceintegrations.common.ValueObject;

import static java.util.Objects.requireNonNull;

public class ServerToServerCrossLaunchDevice extends ValueObject {

    private final String deviceId;
    private final String displayName;

    @JsonCreator
    public ServerToServerCrossLaunchDevice(@JsonProperty("deviceId") String deviceId,
                                           @JsonProperty("displayName") String displayName) {
        this.deviceId = requireNonNull(deviceId);
        this.displayName = displayName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public Optional<String> getDisplayName() {
        return Optional.ofNullable(displayName);
    }
}
