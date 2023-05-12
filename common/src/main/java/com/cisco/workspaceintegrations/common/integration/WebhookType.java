package com.cisco.workspaceintegrations.common.integration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import com.cisco.workspaceintegrations.common.ValueObject;

public class WebhookType extends ValueObject {

    public static final WebhookType HMAC_SIGNATURE = new WebhookType(Enum.hmac_signature);
    public static final WebhookType BASIC_AUTHENTICATION = new WebhookType(Enum.basic_authentication);
    public static final WebhookType AUTHORIZATION_HEADER = new WebhookType(Enum.authorization_header);
    public static final WebhookType NONE = new WebhookType(Enum.none);

    private final Enum stateEnum;
    private final String value;

    @JsonCreator
    public WebhookType(String value) {
        stateEnum = Enum.forValue(value);
        this.value = stateEnum == Enum.unknown ? value : stateEnum.toString();
    }

    private WebhookType(Enum stateEnum) {
        this.stateEnum = stateEnum;
        value = stateEnum.toString();
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
        hmac_signature,
        basic_authentication,
        authorization_header,
        none,
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
