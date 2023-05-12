package com.cisco.workspaceintegrations.common.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import static java.util.Objects.requireNonNull;

public final class ActionMessage extends Message {

    public static final String MESSAGE_TYPE = "action";

    private final String jwt;

    @JsonCreator
    public ActionMessage(@JsonProperty("jwt") String jwt) {
        this.jwt = requireNonNull(jwt);
    }

    public String getJwt() {
        return jwt;
    }

    @Override
    public String type() {
        return MESSAGE_TYPE;
    }

    @Override
    public String toString() {
        return "{jwt=** REDACTED **, type=" + type() + "}";
    }
}
