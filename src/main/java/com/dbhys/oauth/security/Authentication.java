package com.dbhys.oauth.security;

/**
 * Created by Milas on 2019/3/29.
 */
public class Authentication {

    private String identityId;

    public Authentication(String identityId){
        this.identityId = identityId;
    }

    public String getIdentityId(){
        return identityId;
    }

}
