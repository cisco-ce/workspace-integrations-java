package com.cisco.workspaceintegrations.common.jwt;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jwt.SignedJWT;

import com.cisco.workspaceintegrations.common.json.Json;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class JWT {

    private static final JWSAlgorithm JWS_ALGORITHM = JWSAlgorithm.ES256;
    private static final String ENCRYPTION_ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_BLOCK_SIZE = 128;
    private static final byte[] AAD = {92, 122, 53, 98, -121, -34, 12, 16};

    private JWT() {
    }

    public static VerifiedJWS verifyJWS(String jws, JWSVerificationKeys keys) throws JWTInvalidException, JWTProcessingException {
        return new VerifiedJWS(jws, keys);
    }

    public static VerifiedJWS jws(String jws) throws JWTInvalidException, JWTProcessingException {
        return new VerifiedJWS(jws);
    }

    private static String decryptClaim(String encrypted, SecretKey key) throws JWTProcessingException, JWTInvalidException {
        checkNotNull(encrypted);
        checkNotNull(key);
        try {
            String[] parts = encrypted.split("\\.");
            checkArgument(parts.length == 2, "Invalid encrypted data");
            byte[] iv = Base64.getUrlDecoder().decode(parts[0]);
            byte[] data = Base64.getUrlDecoder().decode(parts[1]);

            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(GCM_BLOCK_SIZE, iv));
            cipher.updateAAD(AAD);
            return new String(cipher.doFinal(data), StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalStateException e) {
            throw new JWTProcessingException("Unable to decrypt", e);
        } catch (InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException |
                 IllegalArgumentException e) {
            throw new JWTInvalidException("Invalid encrypted data", e);
        }
    }

    public static final class VerifiedJWS {

        private JWSVerificationKeys.VerificationKey key;
        private final Map<String, Object> headers;
        private final Map<String, Object> claims;
        private final SignedJWT signed;

        private VerifiedJWS(String jwt) {
            this(jwt, null);
        }

        private VerifiedJWS(String jwt, JWSVerificationKeys keys) {
            try {
                signed = SignedJWT.parse(checkNotNull(jwt));
                headers = signed.getHeader().toJSONObject();
                claims = signed.getJWTClaimsSet().toJSONObject();
            } catch (ParseException ex) {
                throw new JWTInvalidException("Invalid JWT", ex);
            }
            if (!JWS_ALGORITHM.getName().equals(headers.get("alg"))) {
                throw new JWTInvalidException("Unsupported JWS algorithm");
            }
            if (headers.get("kid") == null) {
                throw new JWTInvalidException("Malformed JWT");
            }
            if (keys != null) {
                verifySignature(keys);
            }
        }

        public void verifySignature(JWSVerificationKeys keys) {
            key = checkNotNull(keys).verificationKey(String.valueOf(headers.get("kid")));
            try {
                if (!signed.verify(new ECDSAVerifier(key.verificationKey()))) {
                    throw new JWTInvalidException("Invalid signature");
                }
            } catch (JOSEException e) {
                throw new JWTInvalidException("Invalid JWT", e);
            }
        }

        public boolean canDecryptClaims() throws JWTInvalidException, JWTProcessingException {
            return key.decryptionKey().isPresent();
        }

        public <T> Optional<T> header(String name, Class<T> type) {
            return Optional.ofNullable(headers.get(name)).map(value -> fromMiniDev(value, type));
        }

        public <T> Optional<T> claim(String name, Class<T> type) {
            return Optional.ofNullable(claims.get(name)).map(value -> fromMiniDev(value, type));
        }

        public <T> Optional<T> encryptedClaim(String name, Class<T> type) {
            return Optional.ofNullable(claims.get(name))
                           .map(value -> decryptClaim(String.valueOf(value), decryptionKey()))
                           .map(value -> Json.fromJsonString(value, type));
        }

        private SecretKey decryptionKey() {
            return key.decryptionKey().orElseThrow(
                () -> new JWTProcessingException("Not authorized to see private claims")
            );
        }

    }

    @SuppressWarnings("unchecked")
    private static <T> T fromMiniDev(Object value, Class<T> type) {
        if (type.isInstance(value)) {
            return (T) value;
        }
        return Json.fromJsonString(value.toString(), type);
    }
}
