package com.cisco.workspaceintegrations.common.integration.features;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.cisco.workspaceintegrations.common.Utils;
import com.cisco.workspaceintegrations.common.ValueObject;

import static com.google.common.base.Preconditions.checkNotNull;

public class ServerToServerCrossLaunchRequest extends ValueObject {

    private final CrossLaunchTask task;
    private final List<ServerToServerCrossLaunchDevice> devices;
    private final String jwt;

    @JsonCreator
    public ServerToServerCrossLaunchRequest(@JsonProperty(value = "task", required = true) CrossLaunchTask task,
                                            @JsonProperty(value = "jwt", required = true) String jwt,
                                            @JsonProperty(value = "devices", required = true) List<ServerToServerCrossLaunchDevice>
                                                devices) {
        this.task = checkNotNull(task);
        this.devices = Utils.toUnmodifiableList(checkNotNull(devices));
        this.jwt = checkNotNull(jwt);
    }

    public CrossLaunchTask getTask() {
        return task;
    }

    public List<ServerToServerCrossLaunchDevice> getDevices() {
        return devices;
    }

    public String getJwt() {
        return jwt;
    }
}
