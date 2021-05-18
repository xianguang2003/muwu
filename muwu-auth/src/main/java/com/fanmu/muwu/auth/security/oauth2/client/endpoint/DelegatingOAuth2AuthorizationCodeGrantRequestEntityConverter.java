/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.security.oauth2.client.endpoint;


import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Maps;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

/**
 * A {@link Converter} that converts the provided {@link OAuth2AuthorizationCodeGrantRequest}
 * to a {@link RequestEntity} representation of an OAuth 2.0 Access Token Request
 * for the Authorization Code Grant.
 *
 * @author Joe Grandja
 * @see Converter
 * @see OAuth2AuthorizationCodeGrantRequest
 * @see RequestEntity
 * @since 5.1
 */
@Component
public class DelegatingOAuth2AuthorizationCodeGrantRequestEntityConverter implements Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> {

    private Map<String, Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>>> authorizationCodeGrantRequestEntityConverters = Maps.newHashMap();

    private Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> requestEntityConverter = new OAuth2AuthorizationCodeGrantRequestEntityConverter();

    public DelegatingOAuth2AuthorizationCodeGrantRequestEntityConverter(List<AbstractOAuth2AuthorizationCodeGrantRequestConverter> authorizationCodeGrantRequestConverters) {
        if (CollUtil.isNotEmpty(authorizationCodeGrantRequestConverters)) {
            for (AbstractOAuth2AuthorizationCodeGrantRequestConverter authorizationCodeGrantRequestConverter : authorizationCodeGrantRequestConverters) {
                authorizationCodeGrantRequestEntityConverters.put(authorizationCodeGrantRequestConverter.getHandleType(), authorizationCodeGrantRequestConverter);
            }
        }
    }

    /**
     * Returns the {@link RequestEntity} used for the Access Token Request.
     *
     * @param authorizationCodeGrantRequest the authorization code grant request
     * @return the {@link RequestEntity} used for the Access Token Request
     */
    @Override
    public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
        ClientRegistration clientRegistration = authorizationCodeGrantRequest.getClientRegistration();
        Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> converter = authorizationCodeGrantRequestEntityConverters.get(clientRegistration.getRegistrationId());

        if (converter == null) {
            converter = requestEntityConverter;
        }

        return converter.convert(authorizationCodeGrantRequest);
    }
}
