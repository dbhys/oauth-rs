package com.dbhys.oauth.security;

import java.util.Date;

/**
 * Created by Milas on 2019/3/29.
 */
public class Authentication {

    private String username;
    private String name;
    private Date issuerAt;
    private Date expireAt;

    public Authentication(String username){
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getIssuerAt() {
        return issuerAt;
    }

    public void setIssuerAt(Date issuerAt) {
        this.issuerAt = issuerAt;
    }

    public Date getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Date expireAt) {
        this.expireAt = expireAt;
    }
}
