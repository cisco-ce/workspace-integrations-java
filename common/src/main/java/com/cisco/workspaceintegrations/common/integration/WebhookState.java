package com.cisco.workspaceintegrations.common.integration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import com.cisco.workspaceintegrations.common.ValueObject;

public class WebhookState extends ValueObject {
    public static final WebhookState SUCCESS = new WebhookState(Enum.success);
    public static final WebhookState FAILURE = new WebhookState(Enum.failure);
    public static final WebhookState NOT_APPLICABLE = new WebhookState(Enum.not_applicable);

    private final Enum stateEnum;
    private final String value;

    @JsonCreator
    public WebhookState(String value) {
        this.stateEnum = Enum.forValue(value);
        this.value = stateEnum == Enum.unknown ? value : stateEnum.toString();
    }

    public WebhookState(Enum stateEnum) {
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
        success,
        failure,
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
