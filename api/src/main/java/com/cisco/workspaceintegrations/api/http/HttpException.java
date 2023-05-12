package com.cisco.workspaceintegrations.api.http;

import java.net.URI;
import java.util.Optional;

public class HttpException extends RuntimeException {
    private final int statusCode;
    private final String errorBody;
    private final String trackingId;

    public HttpException(String httpMethod, URI uri, Throwable cause) {
        super(String.format("%s request failed, URI: %s, Error: %s",
                            httpMethod, uri, cause.getMessage()), cause);
        this.statusCode = 0;
        this.errorBody = null;
        this.trackingId = null;
    }

    public HttpException(int statusCode, String httpMethod, String errorBody, URI uri, String trackingId) {
        super(String.format("%s request failed, Status: %s, TrackingID: %s, URI: %s, Body: %s",
                            httpMethod, statusCode, trackingId, uri, errorBody));
        this.statusCode = statusCode;
        this.errorBody = errorBody;
        this.trackingId = trackingId;
    }

    public final int getStatusCode() {
        return this.statusCode;
    }

    public boolean isHttpError() {
        return this.statusCode > 0;
    }

    public boolean isServerError() {
        return this.statusCode >= 500 && this.statusCode < 600;
    }

    public boolean isClientError() {
        return this.statusCode >= 400 && this.statusCode < 500;
    }

    public boolean isInternalServerError() {
        return this.statusCode == 500;
    }

    public boolean isBadGateway() {
        return this.statusCode == 502;
    }

    public boolean isServiceUnavailable() {
        return this.statusCode == 503;
    }

    public boolean isGatewayTimeout() {
        return this.statusCode == 504;
    }

    public boolean isBadRequest() {
        return this.statusCode == 400;
    }

    public boolean isNotFound() {
        return this.statusCode == 404;
    }

    public boolean isMethodNotAllowed() {
        return this.statusCode == 405;
    }

    public boolean isUnauthorized() {
        return this.statusCode == 401;
    }

    public boolean isForbidden() {
        return this.statusCode == 403;
    }

    public boolean isConflict() {
        return this.statusCode == 409;
    }

    public boolean isLocked() {
        return this.statusCode == 423;
    }

    public boolean isPreconditionFailed() {
        return this.statusCode == 412;
    }

    public boolean isTooManyRequests() {
        return this.statusCode == 429;
    }

    public Optional<String> getErrorBody() {
        return Optional.ofNullable(this.errorBody);
    }

    public Optional<String> getTrackingId() {
        return Optional.ofNullable(trackingId);
    }
}
