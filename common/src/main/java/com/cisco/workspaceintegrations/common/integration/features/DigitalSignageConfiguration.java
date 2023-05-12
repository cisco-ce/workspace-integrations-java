package com.cisco.workspaceintegrations.common.integration.features;

import java.net.URI;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.cisco.workspaceintegrations.common.ValueObject;

import static com.google.common.base.Preconditions.checkNotNull;

public class DigitalSignageConfiguration extends ValueObject implements ConfigFeatureWithCrossLaunch {

    private final URI signageUrl;
    private final CrossLaunch crossLaunch;

    @JsonCreator
    public DigitalSignageConfiguration(@JsonProperty(value = "signageUrl", required = true) URI signageUrl,
                                       @JsonProperty(value = "crossLaunch") CrossLaunch crossLaunch) {
        this.signageUrl = checkNotNull(signageUrl);
        this.crossLaunch = crossLaunch;
    }

    public URI getSignageUrl() {
        return signageUrl;
    }

    @Override
    public Optional<CrossLaunch> getCrossLaunch() {
        return Optional.ofNullable(crossLaunch);
    }

    public Builder copy() {
        return new Builder(this);
    }

    public static class Builder {

        private URI signageUrl;
        private CrossLaunch crossLaunch;

        private Builder() {
        }

        public Builder(DigitalSignageConfiguration configuration) {
            this.signageUrl = configuration.signageUrl;
            this.crossLaunch = configuration.crossLaunch;
        }

        public Builder signageUrl(URI signageUrl) {
            this.signageUrl = signageUrl;
            return this;
        }

        public Builder crossLaunch(CrossLaunch crossLaunch) {
            this.crossLaunch = crossLaunch;
            return this;
        }

        public DigitalSignageConfiguration build() {
            return new DigitalSignageConfiguration(signageUrl, crossLaunch);
        }
    }
}
