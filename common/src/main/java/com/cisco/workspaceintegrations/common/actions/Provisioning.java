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
}
