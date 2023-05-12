package com.cisco.workspaceintegrations.common.workspaces;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import com.cisco.workspaceintegrations.common.ValueObject;

public class CallingType extends ValueObject {

    public static final CallingType DEFAULT = new CallingType(Enum.freeCalling);
    public static final CallingType FREE_CALLING = new CallingType(Enum.freeCalling);
    public static final CallingType NONE = new CallingType(Enum.none);
    public static final CallingType THIRD_PARTY_SIP = new CallingType(Enum.thirdPartySipCalling);
    public static final CallingType HYBRID_CALLING = new CallingType(Enum.hybridCalling);
    public static final CallingType WEBEX_CALLING = new CallingType(Enum.webexCalling);
    public static final CallingType WEBEX_EDGE_FOR_DEVICES = new CallingType(Enum.webexEdgeForDevices);
    public static final CallingType BROADWORKS_CALLING = new CallingType(Enum.broadworksCalling);

    private final Enum typeEnum;
    private final String value;

    private CallingType(String value) {
        this.typeEnum = Enum.forValue(value);
        this.value = this.typeEnum == Enum.unknown ? value : this.typeEnum.toString();
    }

    public CallingType(Enum typeEnum) {
        this.typeEnum = typeEnum;
        this.value = typeEnum.toString();
    }

    @JsonCreator
    public static CallingType forValue(String value) {
        return new CallingType(value);
    }

    public void throwIfUnknown() {
        if (typeEnum == Enum.unknown) {
            throw new IllegalArgumentException("Invalid calling type");
        }
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
        freeCalling,
        none,
        thirdPartySipCalling,
        webexCalling,
        hybridCalling,
        webexEdgeForDevices,
        broadworksCalling;

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
            return freeCalling;
        }
    }
}
