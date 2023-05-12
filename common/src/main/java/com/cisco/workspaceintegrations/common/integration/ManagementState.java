package com.cisco.workspaceintegrations.common.integration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import com.cisco.workspaceintegrations.common.ValueObject;

public class ManagementState extends ValueObject {

    public static final ManagementState SUCCESS = new ManagementState(Enum.success);
    public static final ManagementState FAILED = new ManagementState(Enum.failed);
    public static final ManagementState NOT_APPLICABLE = new ManagementState(Enum.not_applicable);

    private final Enum typeEnum;
    private final String value;

    @JsonCreator
    public ManagementState(String value) {
        this.typeEnum = Enum.forValue(value);
        this.value = typeEnum == Enum.unknown ? value : typeEnum.toString();
    }

    public ManagementState(Enum typeEnum) {
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
        success,
        failed,
        not_applicable,
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
