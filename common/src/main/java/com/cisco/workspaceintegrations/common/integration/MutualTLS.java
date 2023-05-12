package com.cisco.workspaceintegrations.common.integration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.cisco.workspaceintegrations.common.ValueObject;

import static com.cisco.workspaceintegrations.common.Utils.toPiiLengthString;
import static com.google.common.base.Preconditions.checkNotNull;

public class MutualTLS extends ValueObject {

    private final String certificate;
    private final String privateKey;

    @JsonCreator
    public MutualTLS(@JsonProperty(value = "certificate") String certificate,
                     @JsonProperty(value = "privateKey") String privateKey) {
        this.certificate = checkNotNull(certificate);
        this.privateKey = checkNotNull(privateKey);
    }

    public String getCertificate() {
        return certificate;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    @Override
    public String toString() {
        return "{"
            + "certificate=" + toPiiLengthString(certificate)
            + ", privateKey=" + toPiiLengthString(privateKey)
            + "}";
    }
}
