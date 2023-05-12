package com.cisco.workspaceintegrations.common.integration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import com.cisco.workspaceintegrations.common.ValueObject;

public final class TokensState extends ValueObject {

    public static final TokensState VALID = new TokensState(Enum.valid);
    public static final TokensState INVALID = new TokensState(Enum.invalid);
    public static final TokensState NOT_APPLICABLE = new TokensState(Enum.not_applicable);
    public static final TokensState UNKNOWN = new TokensState(Enum.unknown);

    private final Enum typeEnum;
    private final String value;

    @JsonCreator
    public TokensState(String value) {
        this.typeEnum = Enum.forValue(value);
        this.value = typeEnum == Enum.unknown ? value : typeEnum.toString();
    }

    public TokensState(Enum typeEnum) {
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

    public enum Enum {
        valid,
        invalid,
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
