package com.cisco.workspaceintegrations.common.integration;

import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.cisco.workspaceintegrations.common.ValueObject;

import static java.util.Objects.requireNonNull;

public final class HealthCheckResult extends ValueObject {
    private final boolean success;
    private final ManagementState managementState;
    private final OperationalState operationalState;
    private final TokensState tokensState;
    private final WebhookState webhookState;

    @JsonCreator
    public HealthCheckResult(@JsonProperty("managementState") ManagementState managementState,
                             @JsonProperty("operationalState") OperationalState operationalState,
                             @JsonProperty("tokensState") TokensState tokensState,
                             @JsonProperty("webhookState") WebhookState webhookState) {
        success = isSuccess(managementState, operationalState, tokensState, webhookState);
        this.managementState = requireNonNull(managementState);
        this.operationalState = operationalState;
        this.tokensState = tokensState;
        this.webhookState = requireNonNull(webhookState);
    }

    public static HealthCheckResult managementSuccess(OperationalState operationalState,
                                                      TokensState tokensState,
                                                      WebhookState webhookState) {
        return new HealthCheckResult(ManagementState.SUCCESS, operationalState, tokensState, webhookState);
    }

    public static HealthCheckResult webhookOnlyResult(WebhookState webhookState) {
        return new HealthCheckResult(ManagementState.NOT_APPLICABLE, OperationalState.NOT_APPLICABLE, TokensState.NOT_APPLICABLE,
                                     webhookState);
    }

    private static boolean isSuccess(ManagementState managementState, OperationalState operationalState,
                                     TokensState tokensState, WebhookState webhookState) {
        return isSuccessOrNotApplicable(managementState)
            && isSuccessOrNotApplicable(operationalState)
            && isSuccessOrNotApplicable(tokensState)
            && !webhookState.equals(WebhookState.FAILURE);
    }

    public static HealthCheckResult managementFailed(WebhookState webhookState) {
        return new HealthCheckResult(ManagementState.FAILED, OperationalState.UNKNOWN, TokensState.UNKNOWN, webhookState);
    }

    public boolean isSuccess() {
        return success;
    }

    public ManagementState getManagementState() {
        return managementState;
    }

    public Optional<OperationalState> getOperationalState() {
        return Optional.ofNullable(operationalState);
    }

    public Optional<TokensState> getTokensState() {
        return Optional.ofNullable(tokensState);
    }

    public WebhookState getWebhookState() {
        return webhookState;
    }

    private static boolean isSuccessOrNotApplicable(ManagementState state) {
        return Objects.equals(ManagementState.SUCCESS, state) || Objects.equals(ManagementState.NOT_APPLICABLE, state);
    }

    private static boolean isSuccessOrNotApplicable(OperationalState state) {
        return Objects.equals(OperationalState.OPERATIONAL, state) || Objects.equals(OperationalState.NOT_APPLICABLE, state);
    }

    private static boolean isSuccessOrNotApplicable(TokensState state) {
        return Objects.equals(TokensState.VALID, state) || Objects.equals(TokensState.NOT_APPLICABLE, state);
    }
}
