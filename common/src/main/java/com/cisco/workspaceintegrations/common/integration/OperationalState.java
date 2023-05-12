package com.cisco.workspaceintegrations.common.integration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import com.cisco.workspaceintegrations.common.ValueObject;

public final class OperationalState extends ValueObject {

    public static final OperationalState OPERATIONAL = new OperationalState(Enum.operational);
    public static final OperationalState IMPAIRED = new OperationalState(Enum.impaired);
    public static final OperationalState OUTAGE = new OperationalState(Enum.outage);
    public static final OperationalState TOKEN_INVALID = new OperationalState(Enum.token_invalid);
    public static final OperationalState NOT_APPLICABLE = new OperationalState(Enum.not_applicable);
    public static final OperationalState UNKNOWN = new OperationalState(Enum.unknown);

    private final Enum typeEnum;
    private final String value;

    @JsonCreator
    public OperationalState(String value) {
        this.typeEnum = Enum.forValue(value);
        this.value = typeEnum == Enum.unknown ? value : typeEnum.toString();
    }

    public OperationalState(Enum typeEnum) {
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
        operational,
        impaired,
        outage,
        token_invalid,
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
