package com.cisco.workspaceintegrations.common.workspaces;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.cisco.workspaceintegrations.common.ValueObject;

import static com.google.common.base.Preconditions.checkNotNull;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class Calling extends ValueObject {
    private final CallingType type;
    private final HybridCalling hybridCalling;

    public Calling() {
        this(CallingType.DEFAULT);
    }

    public Calling(CallingType callingType) {
        checkNotNull(callingType);
        this.type = callingType;
        this.hybridCalling = null;
    }

    @JsonCreator
    public Calling(@JsonProperty("type") CallingType type,
                   @JsonProperty("hybridCalling") HybridCalling hybridCalling) {
        this.type = type;
        this.hybridCalling = hybridCalling;
        if (type == null) {
            throw new IllegalArgumentException("Calling type is missing");
        }
    }

    @JsonProperty("type")
    public CallingType getType() {
        return type;
    }

    @JsonProperty("hybridCalling")
    public Optional<HybridCalling> getHybridCalling() {
        return Optional.ofNullable(hybridCalling);
    }

    @Override
    public String toString() {
        return "{type=" + type + "}";
    }
}
