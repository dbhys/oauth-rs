package com.dbhys.oauth.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Milas on 2019/3/29.
 */
public class AuthenticationHelper {
    private final static Logger logger = LoggerFactory.getLogger(AuthenticationHelper.class);

    private static ThreadLocal<Authentication> threadLocal = new ThreadLocal();

    public static void setAuthentication(Authentication authentication){
        threadLocal.set(authentication);
    }

    public static Authentication getAuthentication(){
        Authentication authentication = threadLocal.get();
        logger.debug("username: {}", authentication.getUsername());
        return threadLocal.get();
    }
}
