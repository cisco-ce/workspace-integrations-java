package com.cisco.workspaceintegrations.common.jwt;

import java.security.interfaces.ECPublicKey;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

public final class JWKSetJWSVerificationKeys implements JWSVerificationKeys {

    private static final Logger LOG = LoggerFactory.getLogger(JWKSetJWSVerificationKeys.class);

    private final Map<String, ECPublicKey> keys;

    private JWKSetJWSVerificationKeys(Map<String, ECPublicKey> keys) {
        this.keys = checkNotNull(keys);
    }

    @Override
    public JWSVerificationKeys.VerificationKey verificationKey(String id) throws JWTProcessingException, JWTInvalidException {
        ECPublicKey key = keys.get(id);
        if (key == null) {
            throw new JWTInvalidException("No such key " + id);
        }
        return new VerificationKey(key, Optional.empty());
    }

    public static JWKSetJWSVerificationKeys parse(String jwkSet) {
        JWKSet set;
        try {
            set = JWKSet.parse(jwkSet);
        } catch (ParseException e) {
            throw new JWTProcessingException("Invalid JWK set format", e);
        }
        Map<String, ECPublicKey> publicKeys = new HashMap<>();
        for (JWK jwk : set.getKeys()) {
            if (jwk instanceof ECKey) {
                try {
                    publicKeys.put(jwk.getKeyID(), ((ECKey) jwk).toECPublicKey());
                } catch (JOSEException e) {
                    LOG.error("Error parsing public key " + jwk.getKeyID(), e);
                }
            }
        }
        return new JWKSetJWSVerificationKeys(publicKeys);
    }

}
