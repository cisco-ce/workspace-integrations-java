package com.cisco.workspaceintegrations.common.integration;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.cisco.workspaceintegrations.common.ValueObject;

import static com.cisco.workspaceintegrations.common.Utils.toPiiLengthString;
import static java.util.Objects.requireNonNull;

public final class CustomerDetails extends ValueObject {

    private final String id;
    private final String name;

    @JsonCreator
    public CustomerDetails(@JsonProperty(value = "id", required = true) String id,
                           @JsonProperty("name") String name) {
        this.id = requireNonNull(id);
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    @Override
    public String toString() {
        return "{id=" + id + ", name=" + toPiiLengthString(name) + "}";
    }
}
