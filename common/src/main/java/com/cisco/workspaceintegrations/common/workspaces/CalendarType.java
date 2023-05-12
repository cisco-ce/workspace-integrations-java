package com.cisco.workspaceintegrations.common.workspaces;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import com.cisco.workspaceintegrations.common.ValueObject;

public class CalendarType extends ValueObject {

    public static final CalendarType NONE = new CalendarType(Enum.none);
    public static final CalendarType GOOGLE = new CalendarType(Enum.google);
    public static final CalendarType MICROSOFT = new CalendarType(Enum.microsoft);

    private final Enum typeEnum;
    private final String value;

    private CalendarType(String value) {
        this.typeEnum = Enum.forValue(value);
        this.value = this.typeEnum == Enum.unknown ? value : this.typeEnum.toString();
    }

    public CalendarType(Enum typeEnum) {
        this.typeEnum = typeEnum;
        this.value = typeEnum.toString();
    }

    @JsonCreator
    public static CalendarType forValue(String value) {
        return new CalendarType(value);
    }

    public boolean isUnknown() {
        return typeEnum == Enum.unknown;
    }

    public boolean isNone() {
        return typeEnum == Enum.none;
    }

    @Override
    @JsonValue
    public String toString() {
        return value;
    }

    public Enum toEnum() {
        return typeEnum;
    }

    public enum Enum {
        unknown,
        none,
        google,
        microsoft;

        @JsonCreator
        public static Enum forValue(String value) {
            if (value != null) {
                for (Enum a : values()) {
                    if (a.toString().equalsIgnoreCase(value)) {
                        return a;
                    }
                }
                return unknown;
            }
            return none;
        }
    }
}
