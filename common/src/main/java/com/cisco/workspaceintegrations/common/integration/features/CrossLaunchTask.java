package com.cisco.workspaceintegrations.common.integration.features;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import com.cisco.workspaceintegrations.common.ValueObject;

public class CrossLaunchTask extends ValueObject {

    public static final CrossLaunchTask MANAGE_CONTENT = new CrossLaunchTask(Enum.manage_content);
    public static final CrossLaunchTask ASSIGN_CONTENT = new CrossLaunchTask(Enum.assign_content);

    private final Enum typeEnum;
    private final String value;

    @JsonCreator
    public CrossLaunchTask(String value) {
        this.typeEnum = Enum.forValue(value);
        this.value = typeEnum == Enum.unknown ? value : typeEnum.toString();
    }

    public CrossLaunchTask(Enum typeEnum) {
        this.typeEnum = typeEnum;
        this.value = typeEnum.toString();
    }

    public boolean isUnknown() {
        return typeEnum == Enum.unknown;
    }

    public Enum toEnum() {
        return typeEnum;
    }

    @Override
    @JsonValue
    public String toString() {
        return value;
    }

    public enum Enum {
        manage_content,
        assign_content,
        unknown;

        @JsonCreator
        public static Enum forValue(String value) {
            if (value != null) {
                for (Enum t : values()) {
                    if (t.toString().equalsIgnoreCase(value)) {
                        return t;
                    }
                }
            }
            return unknown;
        }
    }
}
