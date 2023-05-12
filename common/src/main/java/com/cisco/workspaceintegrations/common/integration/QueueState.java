package com.cisco.workspaceintegrations.common.integration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import com.cisco.workspaceintegrations.common.ValueObject;

public class QueueState extends ValueObject {
    public static final QueueState ENABLED = new QueueState(Enum.enabled);
    public static final QueueState DISABLED = new QueueState(Enum.disabled);
    public static final QueueState REMOVE = new QueueState(Enum.remove);

    private final Enum stateEnum;
    private final String value;

    @JsonCreator
    public QueueState(String value) {
        this.stateEnum = Enum.forValue(value);
        this.value = stateEnum == Enum.unknown ? value : stateEnum.toString();
    }

    public QueueState(Enum stateEnum) {
        this.stateEnum = stateEnum;
        this.value = stateEnum.toString();
    }

    @Override
    @JsonValue
    public String toString() {
        return value;
    }

    public Enum toEnum() {
        return stateEnum;
    }

    public enum Enum {
        enabled,
        disabled,
        remove,
        unknown;

        @JsonCreator
        public static Enum forValue(String value) {
            if (value != null) {
                for (Enum a : values()) {
                    if (a.toString().equalsIgnoreCase(value)) {
                        return a;
                    }
                }
            }
            return unknown;
        }
    }
}
