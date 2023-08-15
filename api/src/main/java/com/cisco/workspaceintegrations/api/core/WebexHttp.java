package com.cisco.workspaceintegrations.api.core;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cisco.workspaceintegrations.api.http.Http;
import com.cisco.workspaceintegrations.api.http.HttpException;
import com.cisco.workspaceintegrations.common.actions.Provisioning;
import com.cisco.workspaceintegrations.common.oauth.OAuthClient;

import static com.cisco.workspaceintegrations.common.json.Json.fromJsonString;
import static com.cisco.workspaceintegrations.common.json.Json.toJsonString;
import static java.net.http.HttpRequest.BodyPublishers.ofString;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;

public class WebexHttp {

    private static final Logger LOG = LoggerFactory.getLogger(WebexHttp.class);

    private final Http http;
    private final OAuthClient oAuthClient;
    private Provisioning provisioning;
    private volatile OAuthTokens tokens;
    private final ReentrantLock fetchTokensLock;
    private final ProvisioningChangedListener provisioningChangedListener;

    public WebexHttp(Http http,
                     OAuthClient oAuthClient,
                     Provisioning provisioning,
                     ProvisioningChangedListener provisioningChangedListener) {
        this.http = http;
        this.oAuthClient = oAuthClient;
        this.provisioning = provisioning;
        this.fetchTokensLock = new ReentrantLock();
        this.provisioningChangedListener = provisioningChangedListener;
    }

    public Provisioning getProvisioning() {
        return provisioning;
    }

    public void initTokens() {
        if (tokens == null) {
            try {
                fetchTokensLock.lock();
                if (tokens != null) {
                    return;
                }
                tokens = getNewAccessToken();
            } finally {
                fetchTokensLock.unlock();
            }
            if (!Objects.equals(provisioning.getRefreshToken(), tokens.refreshToken())) {
                this.provisioning = Provisioning.copy(provisioning).refreshToken(tokens.refreshToken()).build();
                try {
                    provisioningChangedListener.refreshTokenChanged(tokens.refreshToken());
                } catch (Exception ex) {
                    LOG.warn("Failure notifying the refresh token change", ex);
                }
            }
        }
    }

    public HttpRequest.Builder newRequestBuilder(URI uri) {
        initTokens();
        return http.defaultRequestBuilder(uri)
                   .header("Accept", "application/json")
                   .header("Content-Type", "application/json")
                   .header("Authorization", "Bearer " + tokens.accessToken());
    }

    public OAuthTokens getNewAccessToken() {
        LOG.info("Fetching a new access token");
        HttpRequest.Builder requestBuilder = http.defaultRequestBuilder(provisioning.getOauthUrl());
        requestBuilder.header("Content-Type", "application/x-www-form-urlencoded");
        requestBuilder.POST(ofString(
            urlEncode(Map.of(
                "grant_type", "refresh_token",
                "refresh_token", provisioning.getRefreshToken(),
                "client_id", oAuthClient.getClientId(),
                "client_secret", oAuthClient.getClientSecret()
            )))
        );
        return fromJsonString(http.execute(requestBuilder.build()).body(), OAuthTokens.class);
    }

    public <Response, Body> Response post(URI uri, Body body, Class<Response> responseType) {
        HttpResponse<String> response = executeWithExpiredTokenRefresh(
            () -> http.execute(newRequestBuilder(uri).POST(ofString(toJsonString(body))).build())
        );
        return fromJsonString(response.body(), responseType);
    }

    public <Body> void post(URI uri, Body body) {
        executeWithExpiredTokenRefresh(
            () -> http.execute(newRequestBuilder(uri).POST(ofString(toJsonString(body))).build())
        );
    }

    public <Response, Body> Response patch(URI uri, Body body, Class<Response> responseType) {
        HttpResponse<String> response = executeWithExpiredTokenRefresh(
            () -> http.execute(newRequestBuilder(uri).method("PATCH", ofString(toJsonString(body))).build())
        );
        return fromJsonString(response.body(), responseType);
    }

    public <Body> void patch(URI uri, Body body) {
        executeWithExpiredTokenRefresh(
            () -> http.execute(newRequestBuilder(uri).method("PATCH", ofString(toJsonString(body))).build())
        );
    }

    public <Response, Body> Response put(URI uri, Body body, Class<Response> responseType) {
        HttpResponse<String> response = executeWithExpiredTokenRefresh(
            () -> http.execute(newRequestBuilder(uri).PUT(ofString(toJsonString(body))).build())
        );
        return fromJsonString(response.body(), responseType);
    }

    public <Body> void put(URI uri, Body body) {
        executeWithExpiredTokenRefresh(
            () -> http.execute(newRequestBuilder(uri).PUT(ofString(toJsonString(body))).build())
        );
    }

    public void delete(URI uri) {
        executeWithExpiredTokenRefresh(
            () -> http.execute(newRequestBuilder(uri).DELETE().build())
        );
    }

    public <Response> Response get(URI uri, Class<Response> responseType) {
        HttpResponse<String> response = executeWithExpiredTokenRefresh(
            () -> http.execute(newRequestBuilder(uri).GET().build())
        );
        return fromJsonString(response.body(), responseType);
    }

    public <Response> Response get(URI uri, TypeReference<Response> responseType) {
        HttpResponse<String> response = executeWithExpiredTokenRefresh(
            () -> http.execute(newRequestBuilder(uri).GET().build())
        );
        return fromJsonString(response.body(), responseType);
    }

    public <Response> Response get(URI uri, Class<Response> responseType, Map<String, Object> params) {
        URI withParams = URI.create(uri + "?" + urlEncode(params));
        HttpResponse<String> response = executeWithExpiredTokenRefresh(
            () -> http.execute(newRequestBuilder(withParams).GET().build())
        );
        return fromJsonString(response.body(), responseType);
    }

    public <Response> Response get(URI uri, TypeReference<Response> responseType, Map<String, Object> params) {
        URI withParams = URI.create(uri + "?" + urlEncode(params));
        HttpResponse<String> response = executeWithExpiredTokenRefresh(
            () -> http.execute(newRequestBuilder(withParams).GET().build())
        );
        return fromJsonString(response.body(), responseType);
    }

    protected static String urlEncode(Map<String, Object> params) {
        return params.entrySet()
                     .stream()
                     .map(entry -> String.join("=",
                                               URLEncoder.encode(entry.getKey(), UTF_8),
                                               URLEncoder.encode(entry.getValue().toString(), UTF_8)))
                     .collect(joining("&"));
    }

    protected <T> T executeWithExpiredTokenRefresh(Supplier<T> request) {
        return executeWithExpiredTokenRefresh(request, false);
    }

    protected <T> T executeWithExpiredTokenRefresh(Supplier<T> request, boolean retry) {
        try {
            return request.get();
        } catch (HttpException ex) {
            if (!retry && ex.isUnauthorized()) {
                LOG.info("Request unauthorized (401), fetching a new access token", ex);
                this.tokens = null;
                this.initTokens();
                return executeWithExpiredTokenRefresh(request, true);
            } else {
                throw ex;
            }
        }
    }
}
