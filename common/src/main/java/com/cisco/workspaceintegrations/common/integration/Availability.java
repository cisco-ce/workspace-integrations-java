package com.cisco.workspaceintegrations.common.integration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import com.cisco.workspaceintegrations.common.ValueObject;

public final class Availability extends ValueObject {

    public static final Availability ORG_PRIVATE = new Availability(Enum.org_private);
    public static final Availability GLOBAL = new Availability(Enum.global);
    public static final Availability UNKNOWN = new Availability(Enum.unknown);

    private final Enum typeEnum;
    private final String value;

    @JsonCreator
    public Availability(String value) {
        this.typeEnum = Enum.forValue(value);
        this.value = typeEnum == Enum.unknown ? value : typeEnum.toString();
    }

    public Availability(Enum typeEnum) {
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
        org_private,
        global,
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
