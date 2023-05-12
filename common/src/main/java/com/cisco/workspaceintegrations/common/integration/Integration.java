package com.cisco.workspaceintegrations.common.integration;

import java.net.URI;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.cisco.workspaceintegrations.common.Utils;
import com.cisco.workspaceintegrations.common.ValueObject;
import com.cisco.workspaceintegrations.common.integration.features.DigitalSignageConfiguration;
import com.cisco.workspaceintegrations.common.integration.features.Feature;
import com.cisco.workspaceintegrations.common.integration.features.PersistentWebAppConfiguration;

import static java.util.Collections.emptySet;
import static java.util.Objects.requireNonNull;

public final class Integration extends ValueObject {

    private final String id;
    private final int manifestVersion;
    private final Set<String> scopes;
    private final Set<String> roles;
    private final XapiAccessKeys xapiAccessKeys;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final Instant expiresAt;
    private final ProvisioningState provisioningState;
    private final URI actionsUrl;
    private final OperationalState operationalState;
    private final CustomerDetails customer;
    private final Webhook webhook;
    private final Set<UUID> locations;
    private final HealthCheckDetails healthCheckDetails;
    private final Set<Feature> features;
    private final DigitalSignageConfiguration digitalSignageConfiguration;
    private final PersistentWebAppConfiguration persistentWebAppConfiguration;
    private final Set<String> publicLocationIds;
    private final QueueWithPollUrl queue;
    private final Availability availability;

    @JsonCreator
    public Integration(
        @JsonProperty(value = "id", required = true) String id,
        @JsonProperty(value = "manifestVersion", required = true) int manifestVersion,
        @JsonProperty(value = "scopes", required = true) Set<String> scopes,
        @JsonProperty(value = "roles", required = true) Set<String> roles,
        @JsonProperty(value = "xapiAccessKeys") XapiAccessKeys xapiAccessKeys,
        @JsonProperty(value = "createdAt", required = true) Instant createdAt,
        @JsonProperty(value = "updatedAt", required = true) Instant updatedAt,
        @JsonProperty(value = "provisioningState", required = true) ProvisioningState provisioningState,
        @JsonProperty(value = "actionsUrl") URI actionsUrl,
        @JsonProperty(value = "operationalState") OperationalState operationalState,
        @JsonProperty(value = "customer") CustomerDetails customer,
        @JsonProperty(value = "webhook") Webhook webhook,
        @JsonProperty(value = "expiresAt") Instant expiresAt,
        @JsonProperty(value = "locations") Set<UUID> locations,
        @JsonProperty(value = "healthCheckDetails") HealthCheckDetails healthCheckDetails,
        @JsonProperty(value = "features") Set<Feature> features,
        @JsonProperty(value = "digitalSignageConfiguration") DigitalSignageConfiguration digitalSignageConfiguration,
        @JsonProperty(value = "persistentWebAppConfiguration") PersistentWebAppConfiguration persistentWebAppConfiguration,
        @JsonProperty(value = "publicLocationIds") Set<String> publicLocationIds,
        @JsonProperty(value = "queue") QueueWithPollUrl queue,
        @JsonProperty(value = "availability") Availability availability) {
        this.id = requireNonNull(id);
        this.manifestVersion = manifestVersion;
        this.scopes = Utils.toUnmodifiableSet(scopes);
        this.roles = Utils.toUnmodifiableSet(roles);
        this.xapiAccessKeys = requireNonNull(xapiAccessKeys);
        this.createdAt = requireNonNull(createdAt);
        this.updatedAt = requireNonNull(updatedAt);
        this.provisioningState = requireNonNull(provisioningState);
        this.actionsUrl = actionsUrl;
        this.operationalState = operationalState;
        this.customer = customer;
        this.webhook = webhook;
        this.expiresAt = expiresAt;
        this.locations = Utils.toUnmodifiableSet(locations);
        this.healthCheckDetails = healthCheckDetails;
        this.features = Utils.toUnmodifiableSet(features);
        this.digitalSignageConfiguration = digitalSignageConfiguration;
        this.persistentWebAppConfiguration = persistentWebAppConfiguration;
        this.publicLocationIds = Utils.toUnmodifiableSet(publicLocationIds);
        this.queue = queue;
        this.availability = availability == null ? Availability.UNKNOWN : availability;
    }

    public String getId() {
        return id;
    }

    public int getManifestVersion() {
        return manifestVersion;
    }

    public Set<String> getScopes() {
        return scopes;
    }

    public XapiAccessKeys getXapiAccessKeys() {
        return xapiAccessKeys;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Optional<Set<String>> getLocationId() {
        return Optional.ofNullable(publicLocationIds);
    }

    public ProvisioningState getProvisioningState() {
        return provisioningState;
    }

    public Optional<URI> getActionsUrl() {
        return Optional.ofNullable(actionsUrl);
    }

    public Optional<OperationalState> getOperationalState() {
        return Optional.ofNullable(operationalState);
    }

    public Optional<CustomerDetails> getCustomer() {
        return Optional.ofNullable(customer);
    }

    public Optional<Webhook> getWebhook() {
        return Optional.ofNullable(webhook);
    }

    public Set<String> getRoles() {
        return roles;
    }

    public Optional<Instant> getExpiresAt() {
        return Optional.ofNullable(expiresAt);
    }

    public Optional<HealthCheckDetails> getHealthCheckDetails() {
        return Optional.ofNullable(healthCheckDetails);
    }

    public Optional<Set<UUID>> getLocations() {
        return Optional.ofNullable(locations);
    }

    public Set<Feature> getFeatures() {
        return features == null ? emptySet() : features;
    }

    public Optional<DigitalSignageConfiguration> getDigitalSignageConfiguration() {
        return Optional.ofNullable(digitalSignageConfiguration);
    }

    public Optional<PersistentWebAppConfiguration> getPersistentWebAppConfiguration() {
        return Optional.ofNullable(persistentWebAppConfiguration);
    }

    public Optional<QueueWithPollUrl> getQueue() {
        return Optional.ofNullable(queue);
    }

    @Override
    public String toString() {
        return "{id=" + id
            + ", manifestVersion=" + manifestVersion
            + ", scopes=" + scopes
            + ", roles=" + roles
            + ", xapiAccessKeys=" + xapiAccessKeys
            + ", createdAt=" + createdAt
            + ", updatedAt=" + updatedAt
            + ", expiresAt=" + expiresAt
            + ", provisioningState=" + provisioningState
            + ", actionsUrl=" + actionsUrl
            + ", operationalState=" + operationalState
            + ", customer=" + customer
            + ", webhook=" + webhook
            + ", locations=" + locations
            + ", healthCheckDetails=" + healthCheckDetails
            + ", features=" + features
            + ", queue=" + queue
            + ", availability=" + availability
            + "}";
    }
}
