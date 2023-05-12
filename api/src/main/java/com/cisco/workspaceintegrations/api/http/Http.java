package com.cisco.workspaceintegrations.api.http;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

public class Http {

    private static final Logger LOG = LoggerFactory.getLogger(Http.class);
    private static final Duration DEFAULT_CONNECTION_TIMEOUT = Duration.ofSeconds(30);

    private final HttpClient client;
    private final String userAgent;

    public Http(String userAgent) {
        this(HttpClient.newBuilder()
                       .connectTimeout(DEFAULT_CONNECTION_TIMEOUT)
                       .build(),
             userAgent
        );
    }

    public Http(HttpClient client, String userAgent) {
        this.client = checkNotNull(client);
        this.userAgent = checkNotNull(userAgent);
    }

    public HttpRequest.Builder defaultRequestBuilder(URI uri) {
        return HttpRequest.newBuilder()
                          .timeout(DEFAULT_CONNECTION_TIMEOUT)
                          .header("User-Agent", userAgent)
                          .uri(uri);
    }

    public HttpResponse<String> execute(HttpRequest request) {
        LOG.debug("{}: {}", request.method(), request.uri());
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new HttpException(request.method(), request.uri(), ex);
        }
        if (response.statusCode() >= 200 && response.statusCode() <= 299) {
            return response;
        }
        String trackingId = response.headers().firstValue("TrackingID").orElse("No TrackingID");
        throw new HttpException(response.statusCode(), request.method(), response.body(), response.uri(), trackingId);
    }

    public HttpClient getClient() {
        return client;
    }

    public String getUserAgent() {
        return userAgent;
    }
}
