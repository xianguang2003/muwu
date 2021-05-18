/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.security.oauth2.client.userinfo.qq;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fanmu.muwu.auth.security.oauth2.client.userinfo.OAuth2UserRequestConverter;
import com.fanmu.muwu.common.util.support.ThreadLocalMap;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Map;

@Component
public class QqOAuth2UserRequestEntityConverter implements OAuth2UserRequestConverter {

    private final String GET_OPEN_ID_URL = "https://graph.qq.com/oauth2.0/me";

    private RestOperations restOperations;

    public QqOAuth2UserRequestEntityConverter() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        restTemplate.setMessageConverters((Arrays.asList(new FastJsonHttpMessageConverter())));
        this.restOperations = restTemplate;
    }


    @Override
    public String getHandleType() {
        return "qq";
    }

    @Override
    public RequestEntity<?> convert(OAuth2UserRequest userRequest) {
        ClientRegistration clientRegistration = userRequest.getClientRegistration();

        HttpMethod httpMethod = HttpMethod.GET;
        MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
        formParameters.add(OAuth2ParameterNames.ACCESS_TOKEN, userRequest.getAccessToken().getTokenValue());
        formParameters.add("fmt", "json");

        // 获取openId
        URI uri = UriComponentsBuilder.fromUriString(GET_OPEN_ID_URL)
                .replaceQueryParams(formParameters).build()
                .toUri();
        RequestEntity<?> request = new RequestEntity<>(httpMethod, uri);
        ResponseEntity<Map> exchange = restOperations.exchange(request, Map.class);
        Map body = exchange.getBody();
        String openid = body.get("openid").toString();
        formParameters.add("openid", openid);
        formParameters.add("oauth_consumer_key", clientRegistration.getClientId());
        // qq临时存储
        ThreadLocalMap.put("qq_openid", openid);

        // 获取用户信息
        uri = UriComponentsBuilder.fromUriString(clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri())
                .replaceQueryParams(formParameters).build()
                .toUri();

        request = new RequestEntity<>(httpMethod, uri);

        return request;
    }
}
