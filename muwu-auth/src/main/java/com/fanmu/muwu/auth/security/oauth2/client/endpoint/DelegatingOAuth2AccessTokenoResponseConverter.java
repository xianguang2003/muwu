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
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.endpoint.MapOAuth2AccessTokenResponseConverter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A {@link Converter} that converts the provided
 * OAuth 2.0 Access Token Response parameters to an {@link OAuth2AccessTokenResponse}.
 *
 * @author Joe Grandja
 * @author Nikita Konev
 * @since 5.3
 */
@Component
public class DelegatingOAuth2AccessTokenoResponseConverter implements Converter<OAuth2AccessTokenoOriginalResponse, OAuth2AccessTokenResponse> {

    private Map<String, Converter<OAuth2AccessTokenoOriginalResponse, OAuth2AccessTokenResponse>> accessTokenoResponseConverters = Maps.newHashMap();

    private MapOAuth2AccessTokenResponseConverter mapOAuth2AccessTokenResponseConverter = new MapOAuth2AccessTokenResponseConverter();

    public DelegatingOAuth2AccessTokenoResponseConverter(List<OAuth2AccessTokenoResponseConverter> accessTokenoResponseConverters) {
        if (CollUtil.isNotEmpty(accessTokenoResponseConverters)) {
            for (OAuth2AccessTokenoResponseConverter accessTokenoResponseConverter : accessTokenoResponseConverters) {
                this.accessTokenoResponseConverters.put(accessTokenoResponseConverter.getHandleType(), accessTokenoResponseConverter);
            }
        }
    }

    /**
     * Returns the {@link RequestEntity} used for the Access Token Request.
     *
     * @param tokenoOriginalResponse the authorization code grant request
     * @return the {@link RequestEntity} used for the Access Token Request
     */
    @Override
    public OAuth2AccessTokenResponse convert(OAuth2AccessTokenoOriginalResponse tokenoOriginalResponse) {
        OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest = tokenoOriginalResponse.getOAuth2AuthorizationCodeGrantRequest();
        ClientRegistration clientRegistration = authorizationCodeGrantRequest.getClientRegistration();
        Converter<OAuth2AccessTokenoOriginalResponse, OAuth2AccessTokenResponse> converter = accessTokenoResponseConverters.get(clientRegistration.getRegistrationId());

        OAuth2AccessTokenResponse accessTokenResponse;
        if (converter == null) {
            accessTokenResponse = mapOAuth2AccessTokenResponseConverter.convert(tokenoOriginalResponse.getResponse()
                    .entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> String.valueOf(entry.getValue()))));
        } else {
            accessTokenResponse = converter.convert(tokenoOriginalResponse);
        }

        return accessTokenResponse;
    }

}
