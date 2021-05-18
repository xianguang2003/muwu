/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fanmu.muwu.auth.security.oauth2.client.Oauth2AuthenticationFailureHandler;
import com.fanmu.muwu.auth.security.oauth2.client.Oauth2AuthenticationSuccessHandler;
import com.fanmu.muwu.auth.security.oauth2.client.endpoint.DelegatingOAuth2AccessTokenoResponseConverter;
import com.fanmu.muwu.auth.security.oauth2.client.endpoint.DelegatingOAuth2AuthorizationCodeGrantRequestEntityConverter;
import com.fanmu.muwu.auth.security.oauth2.client.endpoint.DelegatingOAuth2AuthorizationRequestCustomizer;
import com.fanmu.muwu.auth.security.oauth2.client.endpoint.SimpleAuthorizationCodeTokenResponseClient;
import com.fanmu.muwu.auth.security.oauth2.client.userinfo.DelegatingOAuth2UserRequestEntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.JdbcOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Component
public class Oauth2AuthenticationConfig {

    private final Oauth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;

    private final Oauth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler;

    private final JdbcOperations jdbcOperations;

    private final ClientRegistrationRepository clientRegistrationRepository;

    private final DelegatingOAuth2UserRequestEntityConverter delegatingOAuth2UserRequestEntityConverter;

    private final DelegatingOAuth2AuthorizationCodeGrantRequestEntityConverter delegatingOAuth2AuthorizationCodeGrantRequestEntityConverter;

    private final DelegatingOAuth2AccessTokenoResponseConverter delegatingOAuth2AccessTokenoResponseConverter;

    private final DelegatingOAuth2AuthorizationRequestCustomizer delegatingOAuth2AuthorizationRequestCustomizer;

    @Autowired
    public Oauth2AuthenticationConfig(Oauth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler,
                                      Oauth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler,
                                      JdbcOperations jdbcOperations,
                                      ClientRegistrationRepository clientRegistrationRepository,
                                      DelegatingOAuth2UserRequestEntityConverter delegatingOAuth2UserRequestEntityConverter,
                                      DelegatingOAuth2AuthorizationCodeGrantRequestEntityConverter delegatingOAuth2AuthorizationCodeGrantRequestEntityConverter,
                                      DelegatingOAuth2AccessTokenoResponseConverter delegatingOAuth2AccessTokenoResponseConverter,
                                      DelegatingOAuth2AuthorizationRequestCustomizer delegatingOAuth2AuthorizationRequestCustomizer) {
        this.oauth2AuthenticationSuccessHandler = oauth2AuthenticationSuccessHandler;
        this.oauth2AuthenticationFailureHandler = oauth2AuthenticationFailureHandler;
        this.jdbcOperations = jdbcOperations;
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.delegatingOAuth2UserRequestEntityConverter = delegatingOAuth2UserRequestEntityConverter;
        this.delegatingOAuth2AuthorizationCodeGrantRequestEntityConverter = delegatingOAuth2AuthorizationCodeGrantRequestEntityConverter;
        this.delegatingOAuth2AccessTokenoResponseConverter = delegatingOAuth2AccessTokenoResponseConverter;
        this.delegatingOAuth2AuthorizationRequestCustomizer = delegatingOAuth2AuthorizationRequestCustomizer;
    }

    public void configure(HttpSecurity http) throws Exception {
        http.oauth2Login()
//                .loginProcessingUrl("/qq/callback")
                .successHandler(oauth2AuthenticationSuccessHandler)
                .failureHandler(oauth2AuthenticationFailureHandler)
                .authorizedClientService(authorizedClientService())
                .authorizationEndpoint()
                .authorizationRequestResolver(authorizationRequestResolver())
                .and()
                .tokenEndpoint()
                .accessTokenResponseClient(authorizationCodeTokenResponseClient())
                .and()
                .userInfoEndpoint()
                .userService(userService());
    }

    private JdbcOAuth2AuthorizedClientService authorizedClientService() {
        JdbcOAuth2AuthorizedClientService jdbcOAuth2AuthorizedClientService = new JdbcOAuth2AuthorizedClientService(jdbcOperations, clientRegistrationRepository);
        return jdbcOAuth2AuthorizedClientService;
    }

    private SimpleAuthorizationCodeTokenResponseClient authorizationCodeTokenResponseClient() {
        SimpleAuthorizationCodeTokenResponseClient simpleAuthorizationCodeTokenResponseClient = new SimpleAuthorizationCodeTokenResponseClient();
        simpleAuthorizationCodeTokenResponseClient.setRequestEntityConverter(delegatingOAuth2AuthorizationCodeGrantRequestEntityConverter);
        simpleAuthorizationCodeTokenResponseClient.setResponseOriginaConverter(delegatingOAuth2AccessTokenoResponseConverter);
        return simpleAuthorizationCodeTokenResponseClient;
    }

    private DefaultOAuth2UserService userService() {
        DefaultOAuth2UserService defaultOAuth2UserService = new DefaultOAuth2UserService();
        defaultOAuth2UserService.setRequestEntityConverter(delegatingOAuth2UserRequestEntityConverter);
        RestTemplate restTemplate = new RestTemplate(Arrays.asList(new FormHttpMessageConverter(), new FastJsonHttpMessageConverter()));
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        defaultOAuth2UserService.setRestOperations(restTemplate);
        return defaultOAuth2UserService;
    }

    private OAuth2AuthorizationRequestResolver authorizationRequestResolver() {
        DefaultOAuth2AuthorizationRequestResolver authorizationRequestResolver =
                new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI);
        authorizationRequestResolver.setAuthorizationRequestCustomizer(delegatingOAuth2AuthorizationRequestCustomizer);
        return authorizationRequestResolver;
    }
}
