package com.cisco.workspaceintegrations.common.actions;

import java.net.URI;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.cisco.workspaceintegrations.common.integration.XapiAccessKeys;

import static com.cisco.workspaceintegrations.common.Utils.toUnmodifiableSet;

public class Provisioning extends Action {

    private final String refreshToken;
    private final URI oAuthUrl;
    private final URI appUrl;
    private final URI webexApisBaseUrl;
    private final String orgName;
    private final String region;
    private final Set<String> scopes;
    private final XapiAccessKeys xapiAccess;
    private final URI manifestUrl;

    @JsonCreator
    public Provisioning(@JsonProperty("orgId") String orgId,
                        @JsonProperty("appId") String appId,
                        @JsonProperty("refreshToken") String refreshToken,
                        @JsonProperty("oAuthUrl") URI oAuthUrl,
                        @JsonProperty("appUrl") URI appUrl,
                        @JsonProperty("webexApisBaseUrl") URI webexApisBaseUrl,
                        @JsonProperty("orgName") String orgName,
                        @JsonProperty("region") String region,
                        @JsonProperty("scopes") Set<String> scopes,
                        @JsonProperty("xapiAccess") XapiAccessKeys xapiAccess,
                        @JsonProperty("manifestUrl") URI manifestUrl) {
        super(orgId, appId);
        this.refreshToken = refreshToken;
        this.oAuthUrl = oAuthUrl;
        this.webexApisBaseUrl = webexApisBaseUrl;
        this.appUrl = appUrl;
        this.orgName = orgName;
        this.region = region;
        this.scopes = toUnmodifiableSet(scopes);
        this.xapiAccess = xapiAccess;
        this.manifestUrl = manifestUrl;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder copy(Provisioning copy) {
        Builder builder = new Builder();
        builder.refreshToken = copy.getRefreshToken();
        builder.oauthUrl = copy.getOauthUrl();
        builder.appUrl = copy.getAppUrl();
        builder.webexApisBaseUrl = copy.getWebexApisBaseUrl();
        builder.orgName = copy.getOrgName();
        builder.region = copy.getRegion();
        builder.scopes = copy.getScopes();
        builder.xapiAccess = copy.getXapiAccess();
        builder.manifestUrl = copy.getManifestUrl();
        return builder;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public URI getOauthUrl() {
        return oAuthUrl;
    }

    public URI getAppUrl() {
        return appUrl;
    }

    public String getOrgName() {
        return orgName;
    }

    public URI getWebexApisBaseUrl() {
        return webexApisBaseUrl;
    }

    public String getRegion() {
        return region;
    }

    public Set<String> getScopes() {
        return scopes;
    }

    public XapiAccessKeys getXapiAccess() {
        return xapiAccess;
    }

    public URI getManifestUrl() {
        return manifestUrl;
    }

    public static final class Builder {
        private String orgId;
        private String appId;
        private String refreshToken;
        private URI oauthUrl;
        private URI appUrl;
        private URI webexApisBaseUrl;
        private String orgName;
        private String region;
        private Set<String> scopes;
        private XapiAccessKeys xapiAccess;
        private URI manifestUrl;

        private Builder() {
        }

        public Builder orgId(String val) {
            orgId = val;
            return this;
        }

        public Builder appId(String val) {
            appId = val;
            return this;
        }

        public Builder refreshToken(String val) {
            refreshToken = val;
            return this;
        }

        public Builder oAuthUrl(URI val) {
            oauthUrl = val;
            return this;
        }

        public Builder appUrl(URI val) {
            appUrl = val;
            return this;
        }

        public Builder webexApisBaseUrl(URI val) {
            webexApisBaseUrl = val;
            return this;
        }

        public Builder orgName(String val) {
            orgName = val;
            return this;
        }

        public Builder region(String val) {
            region = val;
            return this;
        }

        public Builder scopes(Set<String> val) {
            scopes = val;
            return this;
        }

        public Builder xapiAccess(XapiAccessKeys val) {
            xapiAccess = val;
            return this;
        }

        public Builder manifestUrl(URI val) {
            manifestUrl = val;
            return this;
        }

        public Provisioning build() {
            return new Provisioning(orgId, appId, refreshToken, oauthUrl, appUrl, webexApisBaseUrl, orgName, region, scopes,
                                    xapiAccess, manifestUrl);
        }
    }
}
