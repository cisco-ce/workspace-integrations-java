package com.cisco.workspaceintegrations.common.jwt;

import java.security.interfaces.ECPublicKey;
import java.util.Optional;
import javax.crypto.SecretKey;

import com.cisco.workspaceintegrations.common.ValueObject;

import static com.google.common.base.Preconditions.checkNotNull;

public interface JWSVerificationKeys {

    VerificationKey verificationKey(String id) throws JWTProcessingException, JWTInvalidException;

    final class VerificationKey extends ValueObject {

        private final ECPublicKey verificationKey;
        private final Optional<SecretKey> decryptionKey;

        public VerificationKey(ECPublicKey verificationKey, Optional<SecretKey> decryptionKey) {
            this.verificationKey = checkNotNull(verificationKey);
            this.decryptionKey = checkNotNull(decryptionKey);
        }

        public ECPublicKey verificationKey() {
            return verificationKey;
        }

        public Optional<SecretKey> decryptionKey() {
            return decryptionKey;
        }

    }

}
