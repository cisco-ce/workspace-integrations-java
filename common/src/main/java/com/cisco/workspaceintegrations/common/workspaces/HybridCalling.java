package com.cisco.workspaceintegrations.common.workspaces;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.cisco.workspaceintegrations.common.ValueObject;

public class HybridCalling extends ValueObject {
    private final String emailAddress;

    @JsonCreator
    public HybridCalling(@JsonProperty("emailAddress") String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public boolean isEmpty() {
        return this.emailAddress == null;
    }

    @JsonProperty("emailAddress")
    public Optional<String> getEmailAddress() {
        return Optional.ofNullable(emailAddress);
    }

}
