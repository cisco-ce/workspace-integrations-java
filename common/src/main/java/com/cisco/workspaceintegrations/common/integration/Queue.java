package com.cisco.workspaceintegrations.common.integration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.cisco.workspaceintegrations.common.ValueObject;

import static com.cisco.workspaceintegrations.common.integration.QueueState.DISABLED;
import static com.cisco.workspaceintegrations.common.integration.QueueState.ENABLED;
import static com.cisco.workspaceintegrations.common.integration.QueueState.REMOVE;

public class Queue extends ValueObject {

    private final QueueState state;

    @JsonCreator
    public Queue(@JsonProperty(value = "state") QueueState state) {
        this.state = state == null ? ENABLED : state;
    }

    public QueueState getState() {
        return state;
    }

    @JsonIgnore
    public boolean isEnabled() {
        return state.equals(ENABLED);
    }

    @JsonIgnore
    public boolean isDisabled() {
        return state.equals(DISABLED);
    }

    @JsonIgnore
    public boolean isRemove() {
        return state.equals(REMOVE);
    }

    @Override
    public String toString() {
        return "{state=" + state + '}';
    }

    public static Queue enabledQueue() {
        return new Queue(ENABLED);
    }

    public static Queue disabledQueue() {
        return new Queue(DISABLED);
    }

    public static Queue removeQueue() {
        return new Queue(REMOVE);
    }
}
