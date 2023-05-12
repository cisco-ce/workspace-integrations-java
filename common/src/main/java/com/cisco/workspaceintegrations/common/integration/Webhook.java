package com.cisco.workspaceintegrations.common.integration;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.cisco.workspaceintegrations.common.ValueObject;

import static com.cisco.workspaceintegrations.common.Utils.toPiiLengthString;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.Collections.unmodifiableMap;

public class Webhook extends ValueObject {

    public static final int MIN_HMAC_SECRET_LENGTH = 20;
    public static final String TARGET_URL_PROP = "targetUrl";
    public static final String SECRET_PROP = "secret";
    public static final String TYPE_PROP = "type";
    public static final String USERNAME_PROP = "username";
    public static final String PASSWORD_PROP = "password";
    public static final String MUTUAL_TLS_PROP = "mutualTLS";
    public static final String HEADERS_PROP = "headers";
    public static final String DISABLED_PROP = "disabled";

    private final URI targetUrl;
    private final String secret;
    private final String username;
    private final String password;
    private final WebhookType type;
    private final MutualTLS mutualTLS;
    private final Map<String, String> headers;
    private final boolean disabled;

    @JsonCreator
    public Webhook(@JsonProperty(value = TARGET_URL_PROP) URI targetUrl,
                   @JsonProperty(value = TYPE_PROP) WebhookType type,
                   @JsonProperty(value = SECRET_PROP) String secret,
                   @JsonProperty(value = USERNAME_PROP) String username,
                   @JsonProperty(value = PASSWORD_PROP) String password,
                   @JsonProperty(value = MUTUAL_TLS_PROP) MutualTLS mutualTLS,
                   @JsonProperty(value = HEADERS_PROP) Map<String, String> headers,
                   @JsonProperty(value = DISABLED_PROP) Boolean disabled) {
        this.targetUrl = WebhookType.NONE.equals(type) ? targetUrl : checkNotNull(targetUrl);
        this.type = type == null ? WebhookType.HMAC_SIGNATURE : type;
        this.secret = secret;
        this.username = username;
        this.password = password;
        this.mutualTLS = mutualTLS;
        this.headers = headers != null ? unmodifiableMap(headers) : null;
        this.disabled = disabled != null && disabled;

        switch (this.type.toEnum()) {
            case none:
                if (targetUrl != null && !isNullOrEmpty(targetUrl.toString())) {
                    throw new IllegalArgumentException("Target URL can't be set for webhook type none");
                }
                if (!isNullOrEmpty(secret)) {
                    throw new IllegalArgumentException("Secret can't be set for webhook type none");
                }
                if (!isNullOrEmpty(username)) {
                    throw new IllegalArgumentException("Username can't be set for webhook type none");
                }
                if (!isNullOrEmpty(password)) {
                    throw new IllegalArgumentException("Password can't be set for webhook type none");
                }
                break;
            case hmac_signature:
                if (isNullOrEmpty(secret)) {
                    throw new IllegalArgumentException("Missing required field secret");
                }
                if (secret.length() < MIN_HMAC_SECRET_LENGTH) {
                    throw new IllegalArgumentException("Secret must be at least " + MIN_HMAC_SECRET_LENGTH + " characters");
                }
                break;
            case authorization_header:
                if (isNullOrEmpty(secret)) {
                    throw new IllegalArgumentException("Missing required field secret");
                }
                break;
            case basic_authentication:
                if (isNullOrEmpty(username) || isNullOrEmpty(password)) {
                    throw new IllegalArgumentException("Add a valid username and password");
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown webhook type");
        }
    }

    public static Webhook forHmacSignature(URI targetUrl, String secret) {
        return new Webhook(targetUrl, WebhookType.HMAC_SIGNATURE, secret, null, null, null, null, false);
    }

    public static Webhook forBasicAuth(URI targetUrl, String username, String password) {
        return new Webhook(targetUrl, WebhookType.BASIC_AUTHENTICATION, null, username, password, null, null, false);
    }

    public static Webhook forAuthHeader(URI targetUrl, String authHeader) {
        return new Webhook(targetUrl, WebhookType.AUTHORIZATION_HEADER, authHeader, null, null, null, null, false);
    }

    public static Webhook forNone(URI targetUrl, String secret, String username, String password) {
        return new Webhook(targetUrl, WebhookType.NONE, secret, username, password, null, null, false);
    }

    public static Webhook forNone() {
        return new Webhook(null, WebhookType.NONE, null, null, null, null, null, false);
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder copy() {
        return builder()
            .targetUrl(targetUrl)
            .type(type)
            .secret(secret)
            .username(username)
            .password(password)
            .mutualTLS(mutualTLS)
            .headers(headers)
            .disabled(disabled);
    }

    public URI getTargetUrl() {
        return targetUrl;
    }

    public String getSecret() {
        return secret;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Optional<MutualTLS> getMutualTLS() {
        return Optional.ofNullable(mutualTLS);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Optional<Map<String, String>> getHeadersAsOptional() {
        return Optional.ofNullable(headers);
    }

    public WebhookType getType() {
        return type;
    }

    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public String toString() {
        return "{"
            + "targetUrl=" + targetUrl
            + ", type='" + type + '\''
            + ", secret='" + toPiiLengthString(secret) + '\''
            + ", username='" + toPiiLengthString(username) + '\''
            + ", password='" + toPiiLengthString(password) + '\''
            + ", mutualTLS=" + mutualTLS
            + ", headers=" + headers
            + ", disabled=" + disabled
            + '}';
    }

    public static final class Builder {
        private URI targetUrl;
        private WebhookType type;
        private String secret;
        private String username;
        private String password;
        private MutualTLS mutualTLS;
        private Map<String, String> headers;
        private boolean disabled;

        public Builder targetUrl(URI targetUrl) {
            this.targetUrl = targetUrl;
            return this;
        }

        public Builder type(WebhookType type) {
            this.type = type;
            return this;
        }

        public Builder secret(String secret) {
            this.secret = secret;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder mutualTLS(MutualTLS mutualTLS) {
            this.mutualTLS = mutualTLS;
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder disabled(boolean disabled) {
            this.disabled = disabled;
            return this;
        }

        public Webhook build() {
            return new Webhook(targetUrl, type, secret, username, password, mutualTLS, headers, disabled);
        }
    }
}
