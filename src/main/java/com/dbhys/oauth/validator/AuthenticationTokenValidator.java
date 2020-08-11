package com.dbhys.oauth.validator;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWEKeySelector;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jwt.*;
import com.nimbusds.jwt.proc.BadJWTException;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
/**
 * Authentication token validator.
 * Created by Milas on 2019/3/19.
 */
public class AuthenticationTokenValidator{
    /**
     * The expected token issuer.
     */
    private final String expectedIssuer;


    /**
     * The JWS key selector.
     */
    private final JWSKeySelector jwsKeySelector;


    /**
     * The JWE key selector.
     */
    private final JWEKeySelector jweKeySelector;
    /**
     * Creates a new abstract JWT validator.
     *
     * @param expectedIssuer The expected token issuer (OpenID Provider).
     *                       Must not be {@code null}.
     * @param jwsKeySelector The key selector for JWS verification,
     *                       {@code null} if unsecured (plain) tokens are
     *                       expected.
     * @param jweKeySelector The key selector for JWE decryption,
     *                       {@code null} if encrypted tokens are not
     */
    public AuthenticationTokenValidator(String expectedIssuer, JWSKeySelector jwsKeySelector, JWEKeySelector jweKeySelector) {
        if (expectedIssuer == null) {
            throw new IllegalArgumentException("The expected token issuer must not be null");
        }
        this.expectedIssuer = expectedIssuer;
        // Optional
        this.jwsKeySelector = jwsKeySelector;
        this.jweKeySelector = jweKeySelector;
    }

    /**
     * Validates the specified ID token.
     *
     * @param jwt       The ID token. Must not be {@code null}.
     *
     * @return The claims set of the verified ID token.
     *
     * @throws BadJOSEException If the ID token is invalid or expired.
     * @throws JOSEException    If an internal JOSE exception was
     *                          encountered.
     */
    public JWTClaimsSet validate(final JWT jwt)
            throws BadJOSEException, JOSEException {

        if (jwt instanceof PlainJWT) {
            return validate((PlainJWT)jwt);
        } else if (jwt instanceof SignedJWT) {
            return validate((SignedJWT) jwt);
        } else if (jwt instanceof EncryptedJWT) {
            return validate((EncryptedJWT) jwt);
        } else {
            throw new JOSEException("Unexpected JWT type: " + jwt.getClass());
        }
    }


    /**
     * Verifies the specified unsecured (plain) ID token.
     *
     * @param jwt       The ID token. Must not be {@code null}.
     *
     * @return The claims set of the verified ID token.
     *
     * @throws BadJOSEException If the ID token is invalid or expired.
     * @throws JOSEException    If an internal JOSE exception was
     *                          encountered.
     */
    private JWTClaimsSet validate(final PlainJWT jwt)
            throws BadJOSEException, JOSEException {

        if (getJWSKeySelector() != null) {
            throw new BadJWTException("Signed ID token expected");
        }

        JWTClaimsSet jwtClaimsSet;

        try {
            jwtClaimsSet = jwt.getJWTClaimsSet();
        } catch (java.text.ParseException e) {
            throw new BadJWTException(e.getMessage(), e);
        }

        //JWTClaimsSetVerifier<?> claimsVerifier = new JWTTokenClaimsVerifier(...));
        //claimsVerifier.verify(jwtClaimsSet, null);
        return jwtClaimsSet;
    }


    /**
     * Verifies the specified signed ID token.
     *
     * @param jwt       The ID token. Must not be {@code null}.
     *
     * @return The claims set of the verified ID token.
     *
     * @throws BadJOSEException If the ID token is invalid or expired.
     * @throws JOSEException    If an internal JOSE exception was
     *                          encountered.
     */
    private JWTClaimsSet validate(final SignedJWT jwt)
            throws BadJOSEException, JOSEException {

        if (getJWSKeySelector() == null) {
            throw new BadJWTException("Verification of signed JWTs not configured");
        }

        ConfigurableJWTProcessor<?> jwtProcessor = new DefaultJWTProcessor();
        jwtProcessor.setJWSKeySelector(getJWSKeySelector());
        //jwtProcessor.setJWTClaimsSetVerifier());
        return jwtProcessor.process(jwt, null);
    }


    /**
     * Verifies the specified signed and encrypted ID token.
     *
     * @param jwt       The ID token. Must not be {@code null}.
     *
     * @return The claims set of the verified ID token.
     *
     * @throws BadJOSEException If the ID token is invalid or expired.
     * @throws JOSEException    If an internal JOSE exception was
     *                          encountered.
     */
    private JWTClaimsSet validate(final EncryptedJWT jwt)
            throws BadJOSEException, JOSEException {

        if (getJWEKeySelector() == null) {
            throw new BadJWTException("Decryption of JWTs not configured");
        }
        if (getJWSKeySelector() == null) {
            throw new BadJWTException("Verification of signed JWTs not configured");
        }

        ConfigurableJWTProcessor<?> jwtProcessor = new DefaultJWTProcessor();
        jwtProcessor.setJWSKeySelector(getJWSKeySelector());
        jwtProcessor.setJWEKeySelector(getJWEKeySelector());
        //jwtProcessor.setJWTClaimsSetVerifier());

        return jwtProcessor.process(jwt, null);
    }

    /**
     * Returns the configured JWS key selector for signed token
     * verification.
     *
     * @return The JWS key selector, {@code null} if none.
     */
    public JWSKeySelector getJWSKeySelector() {
        return jwsKeySelector;
    }


    /**
     * Returns the configured JWE key selector for encrypted token
     * decryption.
     *
     * @return The JWE key selector, {@code null}.
     */
    public JWEKeySelector getJWEKeySelector() {
        return jweKeySelector;
    }

}
