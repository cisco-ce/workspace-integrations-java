package com.cisco.workspaceintegrations.common.integration.features;

import java.net.URI;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.cisco.workspaceintegrations.common.ValueObject;

import static com.google.common.base.Preconditions.checkNotNull;

public class PersistentWebAppConfiguration extends ValueObject implements ConfigFeatureWithCrossLaunch {

    private final URI pwaUrl;
    private final CrossLaunch crossLaunch;
    private final String directXapiAccessAllowListOverride;

    @JsonCreator
    public PersistentWebAppConfiguration(@JsonProperty(value = "pwaUrl", required = true) URI pwaUrl,
                                         @JsonProperty(value = "crossLaunch") CrossLaunch crossLaunch,
                                         @JsonProperty(value = "directXapiAccessAllowListOverride")
                                         String directXapiAccessAllowListOverride) {
        this.pwaUrl = checkNotNull(pwaUrl);
        this.crossLaunch = crossLaunch;
        this.directXapiAccessAllowListOverride = directXapiAccessAllowListOverride;
    }

    public URI getPwaUrl() {
        return pwaUrl;
    }

    @Override
    public Optional<CrossLaunch> getCrossLaunch() {
        return Optional.ofNullable(crossLaunch);
    }

    public Optional<String> getDirectXapiAccessAllowListOverride() {
        return Optional.ofNullable(directXapiAccessAllowListOverride);
    }

    public String getEffectiveDirectXapiAccessAllowList() {
        return getDirectXapiAccessAllowListOverride().orElse(pwaUrl.getHost());
    }

    public Builder copy() {
        return new Builder(this);
    }

    public static class Builder {

        private URI pwaUrl;
        private CrossLaunch crossLaunch;
        private String directXapiAccessAllowListOverride;

        private Builder() {
        }

        public Builder(PersistentWebAppConfiguration configuration) {
            this.pwaUrl = configuration.pwaUrl;
            this.crossLaunch = configuration.crossLaunch;
            this.directXapiAccessAllowListOverride = configuration.directXapiAccessAllowListOverride;
        }

        public Builder pwaUrl(URI pwaUrl) {
            this.pwaUrl = pwaUrl;
            return this;
        }

        public Builder crossLaunch(CrossLaunch crossLaunch) {
            this.crossLaunch = crossLaunch;
            return this;
        }

        public Builder directXapiAccessAllowListOverride(String directXapiAccessAllowList) {
            this.directXapiAccessAllowListOverride = directXapiAccessAllowList;
            return this;
        }

        public PersistentWebAppConfiguration build() {
            return new PersistentWebAppConfiguration(pwaUrl, crossLaunch, directXapiAccessAllowListOverride);
        }
    }
}
