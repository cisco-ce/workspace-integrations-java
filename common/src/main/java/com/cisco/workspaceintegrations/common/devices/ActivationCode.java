package com.cisco.workspaceintegrations.common.devices;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.cisco.workspaceintegrations.common.ValueObject;

import static com.cisco.workspaceintegrations.common.Utils.toPiiLengthString;

public final class ActivationCode extends ValueObject {

    private final String code;
    private final Date expiryTime;

    @JsonCreator
    private ActivationCode(@JsonProperty("code") String code, @JsonProperty("expiryTime") Date expiryTime) {
        this.code = code;
        this.expiryTime = expiryTime;
    }

    public String getCode() {
        return code;
    }

    public Date getExpiryTime() {
        return expiryTime != null ? new Date(expiryTime.getTime()) : null;
    }

    @Override
    public String toString() {
        return "{code=" + toPiiLengthString(code)
            + ", expiryTime=" + expiryTime
            + "}";
    }
}
