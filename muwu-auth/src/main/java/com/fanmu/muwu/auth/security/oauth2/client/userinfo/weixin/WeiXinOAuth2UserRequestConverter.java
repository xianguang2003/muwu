/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.security.oauth2.client.userinfo.weixin;

import com.fanmu.muwu.auth.security.oauth2.client.userinfo.OAuth2UserRequestConverter;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class WeiXinOAuth2UserRequestConverter implements OAuth2UserRequestConverter {

    @Override
    public String getHandleType() {
        return "weixin";
    }

    @Override
    public RequestEntity<?> convert(OAuth2UserRequest userRequest) {
        ClientRegistration clientRegistration = userRequest.getClientRegistration();

        HttpMethod httpMethod = HttpMethod.GET;
        MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
        formParameters.add(OAuth2ParameterNames.ACCESS_TOKEN, userRequest.getAccessToken().getTokenValue());
        formParameters.add("openid", userRequest.getAdditionalParameters().get("openid").toString());
        formParameters.add("lang", "zh_CN");

        URI uri = UriComponentsBuilder.fromUriString(clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri())
                .replaceQueryParams(formParameters).build()
                .toUri();

        RequestEntity<?> request = new RequestEntity<>(httpMethod, uri);

        return request;
    }
}
