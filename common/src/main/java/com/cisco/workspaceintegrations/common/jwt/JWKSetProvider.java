package com.cisco.workspaceintegrations.common.jwt;

import java.net.URI;

public interface JWKSetProvider {
    String getJWKSet(URI uri);
}
