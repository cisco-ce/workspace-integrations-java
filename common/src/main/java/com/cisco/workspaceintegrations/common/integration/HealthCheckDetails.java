package com.cisco.workspaceintegrations.common.integration;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.cisco.workspaceintegrations.common.ValueObject;

public class HealthCheckDetails extends ValueObject {

    private final HealthCheckResult healthCheckResult;
    private final UUID runByUserId;
    private final Instant runTime;

    @JsonCreator
    public HealthCheckDetails(@JsonProperty("healthCheckResult") HealthCheckResult healthCheckResult,
                              @JsonProperty("runByUserId") UUID runByUserId,
                              @JsonProperty("runTime") Instant runTime) {
        this.healthCheckResult = healthCheckResult;
        this.runByUserId = runByUserId;
        this.runTime = runTime;
    }

    public HealthCheckResult getHealthCheckResult() {
        return healthCheckResult;
    }

    public UUID getRunByUserId() {
        return runByUserId;
    }

    public Instant getRunTime() {
        return runTime;
    }

    @Override
    public String toString() {
        return "{healthCheckResult=" + healthCheckResult
            + ", runByUserId=" + runByUserId
            + ", runTime=" + runTime
            + "}";
    }
}
