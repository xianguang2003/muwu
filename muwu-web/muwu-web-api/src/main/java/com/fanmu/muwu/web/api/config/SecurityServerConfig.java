/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.web.api.config;

import com.fanmu.muwu.common.config.properties.MuWuProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
//@EnableOAuth2Sso
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityServerConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private MuWuProperties muWuProperties;

    /**
     * Configure.
     *
     * @param http the http
     * @throws Exception the exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.authenticationProvider(new AnonymousAuthenticationProvider("default"))
                // N.B. exceptionHandling is duplicated in resources.configure() so that
                // it works
                .exceptionHandling().and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf().disable();
        // @formatter:on

        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();
        muWuProperties.getSecurity().getOauth2().getIgnore().getUrls().forEach(url -> registry.antMatchers(url).permitAll());

        registry.and().addFilterBefore(new PlatformAuthenticationProcessingFilter(objectMapper), AbstractPreAuthenticatedProcessingFilter.class);
        registry.anyRequest().authenticated();

        http.headers().frameOptions().disable();
    }

}
