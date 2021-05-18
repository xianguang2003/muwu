/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.config;

import com.fanmu.muwu.common.security.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * 表单登录配置
 */
@Component
public class FormAuthenticationConfig {

    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    private final AuthenticationFailureHandler authenticationFailureHandler;

    private final LogoutSuccessHandler logoutSuccessHandler;

    /**
     * Instantiates a new Form authentication config.
     *
     * @param authenticationSuccessHandler the authentication success handler
     * @param authenticationFailureHandler the authentication failure handler
     * @param logoutSuccessHandler
     */
    @Autowired
    public FormAuthenticationConfig(AuthenticationSuccessHandler authenticationSuccessHandler,
                                    AuthenticationFailureHandler authenticationFailureHandler,
                                    LogoutSuccessHandler logoutSuccessHandler) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;
    }

    /**
     * 表单登录配置.
     *
     * @param http the http
     * @throws Exception the exception
     */
    void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
                .loginProcessingUrl(SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_FORM)
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .and().logout()
                .logoutUrl(SecurityConstants.DEFAULT_LOGOUT_URL)
                .logoutSuccessHandler(logoutSuccessHandler);
    }

}
