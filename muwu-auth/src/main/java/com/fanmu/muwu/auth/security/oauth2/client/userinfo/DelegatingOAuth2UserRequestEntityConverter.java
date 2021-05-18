/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.security.oauth2.client.userinfo;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Maps;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Component
public class DelegatingOAuth2UserRequestEntityConverter implements Converter<OAuth2UserRequest, RequestEntity<?>> {

    private Map<String, Converter<OAuth2UserRequest, RequestEntity<?>>> userRequestEntityConverters = Maps.newHashMap();

    private Converter<OAuth2UserRequest, RequestEntity<?>> requestEntityConverter = new OAuth2UserRequestEntityConverter();

    public DelegatingOAuth2UserRequestEntityConverter(List<OAuth2UserRequestConverter> userRequestConverters) {
        if (CollUtil.isNotEmpty(userRequestConverters)) {
            for (OAuth2UserRequestConverter userRequestConverter : userRequestConverters) {
                userRequestEntityConverters.put(userRequestConverter.getHandleType(), userRequestConverter);
            }
        }
    }

    @Override
    public RequestEntity<?> convert(OAuth2UserRequest userRequest) {
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        Converter<OAuth2UserRequest, RequestEntity<?>> converter = userRequestEntityConverters.get(clientRegistration.getRegistrationId());

        if (converter == null) {
            converter = requestEntityConverter;
        }

        return converter.convert(userRequest);
    }
}
