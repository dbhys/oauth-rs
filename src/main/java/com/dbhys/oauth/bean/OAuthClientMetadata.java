package com.dbhys.oauth.bean;

import com.dbhys.oauth.ParseException;
import com.dbhys.oauth.http.HTTPRequest;
import com.dbhys.oauth.http.HTTPResponse;
import com.dbhys.oauth.util.JSONObjectUtils;
import net.minidev.json.JSONObject;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class OAuthClientMetadata {


    private String issuer;

    private String jwksURI;

    private String authorizationUri;

    private String refreshUri;

    //token_signing_alg_values_supported
    private String[] tokenSigningAlgValuesSupported;

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getAuthorizationUri() {
        return authorizationUri;
    }

    public void setAuthorizationUri(String authorizationUri) {
        this.authorizationUri = authorizationUri;
    }

    public String getRefreshUri() {
        return refreshUri;
    }

    public void setRefreshUri(String refreshUri) {
        this.refreshUri = refreshUri;
    }

    public String getJwksURI() {
        return jwksURI;
    }

    public void setJwksURI(String jwksURI) {
        this.jwksURI = jwksURI;
    }

    public String[] getTokenSigningAlgValuesSupported() {
        return tokenSigningAlgValuesSupported;
    }

    public void setTokenSigningAlgValuesSupported(String[] tokenSigningAlgValuesSupported) {
        this.tokenSigningAlgValuesSupported = tokenSigningAlgValuesSupported;
    }

    public OAuthClientMetadata(String issuer, String[] tokenSigningAlgValuesSupported, String jwkSetURI){
        this.issuer = issuer;
        this.tokenSigningAlgValuesSupported = tokenSigningAlgValuesSupported;
        this.jwksURI =  jwkSetURI;
    }

    public static OAuthClientMetadata resolve(final String issuer,
                                               final int connectTimeout,
                                               final int readTimeout)
            throws Exception {

        URL configURL;

        try {
            URL issuerURL = new URL(issuer);

            // Validate but don't insist on HTTPS, see
            // http://openid.net/specs/openid-connect-core-1_0.html#Terminology
            if (issuerURL.getQuery() != null && ! issuerURL.getQuery().trim().isEmpty()) {
                throw new Exception("The issuer identifier must not contain a query component");
            }

            if (issuerURL.getPath() != null && issuerURL.getPath().endsWith("/")) {
                configURL = new URL(issuerURL + ".well-known/oauth-configuration");
            } else {
                configURL = new URL(issuerURL + "/.well-known/oauth-configuration");
            }

        } catch (MalformedURLException e) {
            throw new Exception("The issuer identifier doesn't represent a valid URL: " + e.getMessage(), e);
        }

        HTTPRequest httpRequest = new HTTPRequest(HTTPRequest.Method.GET, configURL);
        httpRequest.setConnectTimeout(connectTimeout);
        httpRequest.setReadTimeout(readTimeout);

        HTTPResponse httpResponse = httpRequest.send();

        if (httpResponse.getStatusCode() != 200) {
            throw new IOException("Couldn't download OpenID Provider metadata from " + configURL +
                    ": Status code " + httpResponse.getStatusCode());
        }

        JSONObject jsonObject = httpResponse.getContentAsJSONObject();

        OAuthClientMetadata op = OAuthClientMetadata.parse(jsonObject);

        if (! issuer.equals(op.getIssuer())) {
            throw new Exception("The returned issuer doesn't match the expected: " + op.getIssuer());
        }
        return op;
    }

    public static OAuthClientMetadata parse(final JSONObject jsonObject) throws ParseException {
        String issuer = JSONObjectUtils.getURI(jsonObject, "issuer").toString();
        if (StringUtils.isEmpty(issuer))
            throw new IllegalArgumentException("The value must not be null or empty string");
        String jwkSetURI = JSONObjectUtils.getURI(jsonObject, "jwks_uri", null).toString();
        if (StringUtils.isEmpty(jwkSetURI))
            throw new IllegalArgumentException("The public JWK set URI must not be null");

        if (StringUtils.isEmpty(jwkSetURI))
            throw new IllegalArgumentException("The public JWK set URI must not be null");
        String[] tokenSigningAlgValuesSupported = JSONObjectUtils.getStringArray(jsonObject, "token_signing_alg_values_supported");
        if (tokenSigningAlgValuesSupported == null || tokenSigningAlgValuesSupported.length == 0)
            throw new IllegalArgumentException("At least one supported token signing alg must be specified");

        OAuthClientMetadata oAuthClientMetadata = new OAuthClientMetadata(issuer, tokenSigningAlgValuesSupported, jwkSetURI);
        String authorizationUri = JSONObjectUtils.getURI(jsonObject, "authorization_uri", null).toString();
        String refreshUri = JSONObjectUtils.getURI(jsonObject, "refresh_uri", null).toString();
        oAuthClientMetadata.setAuthorizationUri(authorizationUri);
        oAuthClientMetadata.setRefreshUri(refreshUri);
        return oAuthClientMetadata;
    }
}
