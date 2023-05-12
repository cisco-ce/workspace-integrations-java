package com.cisco.workspaceintegrations.common.integration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import com.cisco.workspaceintegrations.common.ValueObject;

public final class ProvisioningState extends ValueObject {

    public static final ProvisioningState IN_PROGRESS = new ProvisioningState(Enum.in_progress);
    public static final ProvisioningState ERROR = new ProvisioningState(Enum.error);
    public static final ProvisioningState COMPLETED = new ProvisioningState(Enum.completed);

    private final Enum typeEnum;
    private final String value;

    @JsonCreator
    public ProvisioningState(String value) {
        this.typeEnum = Enum.forValue(value);
        this.value = typeEnum == Enum.unknown ? value : typeEnum.toString();
    }

    public ProvisioningState(Enum typeEnum) {
        this.typeEnum = typeEnum;
        this.value = typeEnum.toString();
    }

    @Override
    @JsonValue
    public String toString() {
        return value;
    }

    public Enum toEnum() {
        return typeEnum;
    }

    public boolean isUnknown() {
        return typeEnum == Enum.unknown;
    }

    public enum Enum {
        in_progress,
        error,
        completed,
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
