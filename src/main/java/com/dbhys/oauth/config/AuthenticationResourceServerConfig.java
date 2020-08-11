package com.dbhys.oauth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Milas on 2019/3/19.
 */
@Configuration
@ConfigurationProperties("dbhys.oauth")
public class AuthenticationResourceServerConfig {

    /**
     * The default lifespan for cached JWK sets (5 minutes).
     */
    public static final long DEFAULT_LIFESPAN_HOUR = 24;

    /**
     * The default HTTP connect timeout for JWK set retrieval, in
     * milliseconds. Set to 500 milliseconds.
     */
    private static final int DEFAULT_HTTP_CONNECT_TIMEOUT = 500;


    /**
     * The default HTTP read timeout for JWK set retrieval, in
     * milliseconds. Set to 500 milliseconds.
     */
    private static final int DEFAULT_HTTP_READ_TIMEOUT = 500;
    private static final String DEFAULT_COOKIE_NAME_OF_TOKEN = "ac";

    private String issuer;

    // If production env, disable this config, it's just for swagger test or something else.
    private boolean enableCookieToken;
    private String cookieNameOfToken = DEFAULT_COOKIE_NAME_OF_TOKEN;

    private Long lifeSpan = DEFAULT_LIFESPAN_HOUR;

    // The HTTP connect timeout, in milliseconds. Zero implies no timeout. Must not be negative.
    private Integer connectTimeout = DEFAULT_HTTP_CONNECT_TIMEOUT;

    // The HTTP response read timeout, in milliseconds. Zero implies no timeout. Must not be negative.
    private Integer readTimeout = DEFAULT_HTTP_READ_TIMEOUT;

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public Long getLifeSpan() {
        return lifeSpan;
    }

    public void setLifeSpan(Long lifeSpan) {
        this.lifeSpan = lifeSpan;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
    }

    public boolean isEnableCookieToken() {
        return enableCookieToken;
    }

    public void setEnableCookieToken(boolean enableCookieToken) {
        this.enableCookieToken = enableCookieToken;
    }

    public String getCookieNameOfToken() {
        if (cookieNameOfToken == null || cookieNameOfToken.trim().equals("")){
            return DEFAULT_COOKIE_NAME_OF_TOKEN;
        }
        return cookieNameOfToken;
    }

    public void setCookieNameOfToken(String cookieNameOfToken) {
        this.cookieNameOfToken = cookieNameOfToken;
    }
}
