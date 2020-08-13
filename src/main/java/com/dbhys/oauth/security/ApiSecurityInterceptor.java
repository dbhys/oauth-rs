package com.dbhys.oauth.security;

import com.dbhys.oauth.bean.OAuthClientMetadata;
import com.dbhys.oauth.config.AuthenticationResourceServerConfig;
import com.dbhys.oauth.http.HttpHeader;
import com.dbhys.oauth.http.HttpMethod;
import com.dbhys.oauth.http.MediaType;
import com.dbhys.oauth.util.CookieUtil;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.dbhys.oauth.http.HttpStatus;
import com.dbhys.oauth.validator.AuthenticationTokenValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Milas on 2019/3/14.
 */
@Component
public class ApiSecurityInterceptor implements ApplicationContextAware, HandlerInterceptor {
    private final static Logger logger = LoggerFactory.getLogger(ApiSecurityInterceptor.class.toString());

    private static String AUTHENTICATION_HEADER = "Authentication";
    private static String BEARER = "BEARER ";

    private ApplicationContext applicationContext;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().toUpperCase().equals(HttpMethod.OPTIONS.name())) {
            return true;
        }

        final String authenticationHeader = request.getHeader(AUTHENTICATION_HEADER);
        String token = null;
        if (authenticationHeader != null && authenticationHeader.toUpperCase().startsWith(BEARER)) {
            token = authenticationHeader.substring(7);
        }
        AuthenticationResourceServerConfig config = applicationContext.getBean(AuthenticationResourceServerConfig.class);
        if (config.isEnableCookieToken()) {
            Cookie oauthCookie = CookieUtil.getCookieByName(request, config.getCookieNameOfToken());
            if (oauthCookie != null && !StringUtils.isEmpty(oauthCookie.getValue())) {
                token = oauthCookie.getValue();
            }
        }
        if (token == null){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            authErrorHandler(request, response, "login_required", "You should login at first!");
            return false;
        }
        try {
            AuthenticationTokenValidator validator = applicationContext.getBean(AuthenticationTokenValidator.class);
            JWTClaimsSet jwtClaimsSet = validator.validate(SignedJWT.parse(token));
            Authentication authentication = new Authentication(jwtClaimsSet.getSubject());
            authentication.setName(jwtClaimsSet.getStringClaim("name"));
            authentication.setIssuerAt(jwtClaimsSet.getIssueTime());
            authentication.setExpireAt(jwtClaimsSet.getExpirationTime());
            AuthenticationHelper.setAuthentication(authentication);
            return true;
        } catch (Exception e) {
            logger.error("Invalid token: " + authenticationHeader);
            e.printStackTrace();
            response.setStatus(HttpStatus.FORBIDDEN.value());
            authErrorHandler(request, response, "invalid_token", "Invalid token!");
        }
        return false;
    }

    protected boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
    }

    protected void authErrorHandler(HttpServletRequest request, HttpServletResponse response, String error, String errorDescription) throws IOException {
        if(isAjax(request)) {
            responseError(request, response, error, errorDescription);
        } else {
            OAuthClientMetadata oAuthClientMetadata = applicationContext.getBean(OAuthClientMetadata.class);
            String redirectUrl = request.getRequestURL().toString();
            response.sendRedirect(oAuthClientMetadata.getAuthorizationUri() + "?redirect_uri=" + redirectUrl);
            return;
        }
    }

    private void responseError(HttpServletRequest request, HttpServletResponse response,
                               String error, String errorDescription) throws IOException {
        String acceptMediaType = request.getHeader(HttpHeader.ACCEPT);
        String contentType = request.getHeader(HttpHeader.CONTENT_TYPE);
        if (acceptMediaType == null || acceptMediaType.trim().equals("") || acceptMediaType.contains(MediaType.ALL_VALUE)) {
            acceptMediaType = (contentType != null && !contentType.trim().equals("")) ? contentType : MediaType.APPLICATION_JSON_UTF8_VALUE;
        }

        response.resetBuffer();
        if (acceptMediaType.contains(MediaType.APPLICATION_JSON_VALUE) || acceptMediaType.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
            response.getWriter().write(toJson(error, errorDescription));
        } else if (acceptMediaType.contains(MediaType.APPLICATION_XML_VALUE)) {
            response.getWriter().write(toXml(error, errorDescription));
        } else if (acceptMediaType.contains("text/")) {
            response.getWriter().write(toText(error, errorDescription));
        } else {
            response.setHeader(HttpHeader.WWW_AUTHENTICATE, toText(error, errorDescription));
        }
        try {
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AuthenticationHelper.setAuthentication(null);
    }

    private String toJson(String error, String errorDescription) {
        return "{\"error\": \"" + error + "\",\"error_description\" : \"" + errorDescription + "\"}";
    }

    /*private String toHtml(String error, String errorDescription) {
        return "{\"error\": \"" + error + "\",\"error_description\" : \"" + errorDescription + "\"}";
    }*/

    private String toXml(String error, String errorDescription) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><error>" + error + "</error><error_description>" + errorDescription + "</error_description>";
    }

    private String toText(String error, String errorDescription) {
        return "error=\"" + error + "\", error_description=\"" + errorDescription + "\"";
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
