package com.cisco.workspaceintegrations.examples.environmentaldata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cisco.workspaceintegrations.common.oauth.OAuthClient;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    protected Main() {
    }

    public static void main(String[] args) {
        String clientId;
        String secret;
        if (args.length != 2) {
            LOG.info("Usage: <app> <clientId> <clientSecret>");
            return;
        } else {
            clientId = args[0];
            secret = args[1];
        }
        new EnvironmentalData(new OAuthClient(clientId, secret)).run();
    }
}
