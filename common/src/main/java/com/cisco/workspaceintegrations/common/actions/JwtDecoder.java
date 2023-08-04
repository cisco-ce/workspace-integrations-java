package com.cisco.workspaceintegrations.common.actions;

import java.net.URI;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cisco.workspaceintegrations.common.integration.XapiAccessKeys;
import com.cisco.workspaceintegrations.common.json.Json;
import com.cisco.workspaceintegrations.common.jwt.JWKSetJWSVerificationKeys;
import com.cisco.workspaceintegrations.common.jwt.JWKSetProvider;
import com.cisco.workspaceintegrations.common.jwt.JWSVerificationKeys;
import com.cisco.workspaceintegrations.common.jwt.JWT;

public class JwtDecoder {

    private static final Logger LOG = LoggerFactory.getLogger(JwtDecoder.class);
    private static final Map<String, URI> REGIONAL_KEY_SET_URLS = ImmutableMap.of(
        "us-west-2_r", URI.create("https://xapi-r.wbx2.com/jwks"),
        "us-east-2_a", URI.create("https://xapi-a.wbx2.com/jwks"),
        "eu-central-1_k", URI.create("https://xapi-k.wbx2.com/jwks"),
        "us-east-1_int13", URI.create("https://xapi-intb.wbx2.com/jwks"),
        "us-gov-west-1_a1", URI.create("https://xapi.gov.ciscospark.com/jwks")
    );

    private final JWKSetProvider jwkSetProvider;
    private final boolean skipExpiryChecks;

    private JWSVerificationKeys verificationKeys;
    private String defaultRegion = "us-east-2_a";

    public JwtDecoder(JWKSetProvider jwkSetProvider) {
        this(jwkSetProvider, false);
    }

    public JwtDecoder(JWKSetProvider jwkSetProvider, boolean skipExpiryChecks) {
        this.jwkSetProvider = jwkSetProvider;
        this.skipExpiryChecks = skipExpiryChecks;
        if (skipExpiryChecks) {
            LOG.warn("Skipping JWT expiry checks. Do not skip this check in a production environment!");
        }
    }

    public void setDefaultRegion(String defaultRegion) {
        LOG.info("Default region: " + defaultRegion);
        this.defaultRegion = defaultRegion;
    }

    public Action decodeAction(String jwt) {
        JWT.VerifiedJWS verified = getVerifiedJWS(jwt);
        Optional<String> action = verified.claim("action", String.class);
        if (action.isPresent()) {
            String orgId = verified.claim("sub", String.class).get();
            String appId = verified.claim("appId", String.class).get();
            return switch (action.get()) {
                case Action.ACTION_UPDATE_APPROVED ->
                    new UpdateApproved(orgId, appId, verified.claim("manifestVersion", Integer.class).get());
                case Action.ACTION_DEPROVISION -> new Deprovisioning(orgId, appId);
                case Action.ACTION_HEALTH_CHECK -> new HealthCheckRequest(orgId, appId);
                case Action.ACTION_UPDATE -> new Updated(orgId, appId, verified.claim("refreshToken", String.class).orElse(null));
                case Action.ACTION_PROVISION -> {
                    Instant expiryTime = Instant.parse(verified.claim("expiryTime", String.class).get());
                    if (!skipExpiryChecks && expiryTime.isBefore(Instant.now())) {
                        throw new JwtExpiredException("The activation code is expired", expiryTime);
                    }
                    yield new Provisioning(
                        orgId,
                        appId,
                        verified.claim("refreshToken", String.class).get(),
                        URI.create(verified.claim("oauthUrl", String.class).get()),
                        URI.create(verified.claim("appUrl", String.class).get()),
                        URI.create(verified.claim("oauthUrl", String.class).get().replace("/access_token", "")),
                        verified.claim("orgName", String.class).get(),
                        verified.claim("region", String.class).get(),
                        Arrays.stream(verified.claim("scopes", String.class).get().split(","))
                              .collect(Collectors.toSet()),
                        Json.fromJsonString(verified.claim("xapiAccess", String.class).get(), XapiAccessKeys.class),
                        URI.create(verified.claim("manifestUrl", String.class).get()));
                }
                default -> throw new RuntimeException("Unknown action: " + action);
            };
        }
        throw new RuntimeException("No action found in JWT");
    }

    private JWT.VerifiedJWS getVerifiedJWS(String jwt) {
        JWT.VerifiedJWS verified;
        if (this.verificationKeys == null) {
            verified = JWT.jws(jwt);
            URI keySetUrl = verified.claim("region", String.class)
                                    .map(REGIONAL_KEY_SET_URLS::get)
                                    .orElse(REGIONAL_KEY_SET_URLS.get(defaultRegion));
            if (keySetUrl == null) {
                keySetUrl = REGIONAL_KEY_SET_URLS.get("us-east-2_a");
            }
            this.verificationKeys = JWKSetJWSVerificationKeys.parse(jwkSetProvider.getJWKSet(keySetUrl));
            verified.verifySignature(this.verificationKeys);
        } else {
            verified = JWT.verifyJWS(jwt, this.verificationKeys);
        }
        return verified;
    }
}
