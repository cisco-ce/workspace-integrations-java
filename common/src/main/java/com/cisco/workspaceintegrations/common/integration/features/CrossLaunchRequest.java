package com.cisco.workspaceintegrations.common.integration.features;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.cisco.workspaceintegrations.common.Utils;

import static com.google.common.base.Preconditions.checkNotNull;

public class CrossLaunchRequest {

    private final CrossLaunchTask task;
    private final List<CrossLaunchDevice> devices;

    @JsonCreator
    public CrossLaunchRequest(@JsonProperty(value = "task", required = true) CrossLaunchTask task,
                              @JsonProperty(value = "devices", required = true) List<CrossLaunchDevice> devices) {
        this.task = checkNotNull(task);
        this.devices = Utils.toUnmodifiableList(checkNotNull(devices));
    }

    public CrossLaunchTask getTask() {
        return task;
    }

    public List<CrossLaunchDevice> getDevices() {
        return devices;
    }
}
