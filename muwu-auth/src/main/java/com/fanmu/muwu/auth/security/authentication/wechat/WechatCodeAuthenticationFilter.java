/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.security.authentication.wechat;

import com.fanmu.muwu.common.security.SecurityConstants;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 短信登录过滤器
 */
public class WechatCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final String POST = "POST";
    // ~ Static fields/initializers
    // =====================================================================================

    private String codeParameter = SecurityConstants.DEFAULT_PARAMETER_NAME_CODE;
    private String appIdParameter = SecurityConstants.DEFAULT_PARAMETER_NAME_APPID;
    private boolean postOnly = true;

    // ~ Constructors
    // ===================================================================================================

    /**
     * Instantiates a new Sms code authentication filter.
     */
    public WechatCodeAuthenticationFilter() {
        super(new AntPathRequestMatcher(SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_WECHAT, POST));
    }

    // ~ Methods
    // ========================================================================================================

    /**
     * Attempt authentication authentication.
     *
     * @param request  the request
     * @param response the response
     * @return the authentication
     * @throws AuthenticationException the authentication exception
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        if (postOnly && !POST.equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        String code = obtainCode(request);
        if (code == null) {
            code = "";
        }
        code = code.trim();

        String appId = obtainAppId(request);
        if (appId == null) {
            appId = "";
        }
        appId = appId.trim();

        WechatCodeAuthenticationToken authRequest = new WechatCodeAuthenticationToken(code, appId);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }


    /**
     * 获取code
     *
     * @param request the request
     * @return the string
     */
    protected String obtainCode(HttpServletRequest request) {
        return request.getParameter(codeParameter);
    }

    /**
     * 获取appid
     *
     * @param request the request
     * @return the string
     */
    protected String obtainAppId(HttpServletRequest request) {
        return request.getParameter(appIdParameter);
    }

    /**
     * Provided so that subclasses may configure what is put into the
     * authentication request's details property.
     *
     * @param request     that an authentication request is being created for
     * @param authRequest the authentication request object that should have its details            set
     */
    protected void setDetails(HttpServletRequest request, WechatCodeAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    /**
     * Sets the parameter name which will be used to obtain the username from
     * the login request.
     *
     * @param codeParameter the parameter code. Defaults to "code".
     */
    public void setMobileParameter(String codeParameter) {
        Assert.hasText(codeParameter, "Username parameter must not be empty or null");
        this.codeParameter = codeParameter;
    }


    /**
     * Defines whether only HTTP POST requests will be allowed by this filter.
     * If set to true, and an authentication request is received which is not a
     * POST request, an exception will be raised immediately and authentication
     * will not be attempted. The <tt>unsuccessfulAuthentication()</tt> method
     * will be called as if handling a failed authentication.
     * <p>
     * Defaults to <tt>true</tt> but may be overridden by subclasses.
     *
     * @param postOnly the post only
     */
    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    /**
     * Gets mobile parameter.
     *
     * @return the mobile parameter
     */
    public final String getCodeParameter() {
        return codeParameter;
    }

}
