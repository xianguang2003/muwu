/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.web.api.config;

import com.fanmu.muwu.common.config.properties.MuWuProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

/**
 * 资源服务器配置
 */
//@Configuration
//@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final MuWuProperties muWuProperties;

    private final WebResponseExceptionTranslator webResponseExceptionTranslator;

    public ResourceServerConfig(MuWuProperties muWuProperties,
                                WebResponseExceptionTranslator webResponseExceptionTranslator) {
        this.muWuProperties = muWuProperties;
        this.webResponseExceptionTranslator = webResponseExceptionTranslator;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 忽略URL
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();
        muWuProperties.getSecurity().getOauth2().getIgnore().getUrls().forEach(url -> registry.antMatchers(url).permitAll());

        registry.anyRequest().authenticated()
                .and()
                .csrf().disable();

        http.headers().frameOptions().disable();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // 定义异常转换类生效
        OAuth2AuthenticationEntryPoint oAuth2AuthenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
        oAuth2AuthenticationEntryPoint.setExceptionTranslator(webResponseExceptionTranslator);
        resources.authenticationEntryPoint(oAuth2AuthenticationEntryPoint);
    }
}
