package com.cisco.workspaceintegrations.common.integration.features;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import com.cisco.workspaceintegrations.common.ValueObject;

public class Feature extends ValueObject {

    public static final Feature DIGITAL_SIGNAGE = new Feature(Enum.digital_signage);
    public static final Feature PERSISTENT_WEB_APP = new Feature(Enum.persistent_web_app);

    private final Enum typeEnum;
    private final String value;

    @JsonCreator
    public Feature(String value) {
        this.typeEnum = Enum.forValue(value);
        this.value = typeEnum == Enum.unknown ? value : typeEnum.toString();
    }

    public Feature(Enum typeEnum) {
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
        digital_signage,
        persistent_web_app,
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
