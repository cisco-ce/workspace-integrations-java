package com.cisco.workspaceintegrations.common.integration;

import java.net.URI;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.cisco.workspaceintegrations.common.ValueObject;

public final class IntegrationUpdate extends ValueObject {

    private final URI actionsUrl;
    private final ProvisioningState provisioningState;
    private final OperationalState operationalState;
    private final CustomerDetails customer;
    private final Webhook webhook;
    private final Queue queue;

    @JsonCreator
    public IntegrationUpdate(@JsonProperty(value = "actionsUrl") URI actionsUrl,
                             @JsonProperty(value = "provisioningState") ProvisioningState provisioningState,
                             @JsonProperty(value = "operationalState") OperationalState operationalState,
                             @JsonProperty(value = "customer") CustomerDetails customer,
                             @JsonProperty(value = "webhook") Webhook webhook,
                             @JsonProperty(value = "queue") Queue queue) {
        this.actionsUrl = actionsUrl;
        this.provisioningState = provisioningState;
        this.operationalState = operationalState;
        this.customer = customer;
        this.webhook = webhook;
        this.queue = queue;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Optional<URI> getActionsUrl() {
        return Optional.ofNullable(actionsUrl);
    }

    public Optional<ProvisioningState> getProvisioningState() {
        return Optional.ofNullable(provisioningState);
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

    public Optional<Queue> getQueue() {
        return Optional.ofNullable(queue);
    }

    @JsonIgnore
    public boolean hasQueue() {
        return queue != null;
    }

    @Override
    public String toString() {
        return "{actionsUrl=" + actionsUrl
            + ", provisioningState=" + provisioningState
            + ", operationalState=" + operationalState
            + ", customer=" + customer
            + ", webhook=" + webhook
            + ", queue=" + queue
            + "}";
    }


    public static final class Builder {
        private URI actionsUrl;
        private ProvisioningState provisioningState;
        private OperationalState operationalState;
        private CustomerDetails customer;
        private Webhook webhook;
        private Queue queue;

        private Builder() {
        }

        public Builder actionsUrl(URI actionsUrl) {
            this.actionsUrl = actionsUrl;
            return this;
        }

        public Builder provisioningState(ProvisioningState provisioningState) {
            this.provisioningState = provisioningState;
            return this;
        }

        public Builder operationalState(OperationalState operationalState) {
            this.operationalState = operationalState;
            return this;
        }

        public Builder customer(CustomerDetails customer) {
            this.customer = customer;
            return this;
        }

        public Builder webhook(Webhook webhook) {
            this.webhook = webhook;
            return this;
        }

        public Builder queue(Queue queue) {
            this.queue = queue;
            return this;
        }

        public IntegrationUpdate build() {
            return new IntegrationUpdate(actionsUrl, provisioningState, operationalState, customer, webhook, queue);
        }
    }
}
