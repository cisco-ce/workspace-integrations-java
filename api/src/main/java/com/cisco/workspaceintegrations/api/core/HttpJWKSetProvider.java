package com.cisco.workspaceintegrations.api.core;

import java.net.URI;

import com.cisco.workspaceintegrations.api.http.Http;
import com.cisco.workspaceintegrations.common.jwt.JWKSetProvider;

public class HttpJWKSetProvider implements JWKSetProvider {

    private final Http http;

    public HttpJWKSetProvider(Http http) {
        this.http = http;
    }

    @Override
    public String getJWKSet(URI uri) {
        return http.execute(http.defaultRequestBuilder(uri).build()).body();
    }
}
