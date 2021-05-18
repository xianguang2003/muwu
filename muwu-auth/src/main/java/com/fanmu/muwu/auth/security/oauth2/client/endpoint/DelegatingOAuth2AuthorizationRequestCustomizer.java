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
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Component
public class DelegatingOAuth2AuthorizationRequestCustomizer implements Consumer<OAuth2AuthorizationRequest.Builder> {

    private Map<String, Consumer<OAuth2AuthorizationRequest.Builder>> authorizationRequestCustomizers = Maps.newHashMap();

    public DelegatingOAuth2AuthorizationRequestCustomizer(List<OAuth2AuthorizationRequestCustomizer> authorizationRequestCustomizers) {
        if (CollUtil.isNotEmpty(authorizationRequestCustomizers)) {
            for (OAuth2AuthorizationRequestCustomizer authorizationRequestCustomizer : authorizationRequestCustomizers) {
                this.authorizationRequestCustomizers.put(authorizationRequestCustomizer.getHandleType(), authorizationRequestCustomizer);
            }
        }
    }

    @Override
    public void accept(OAuth2AuthorizationRequest.Builder builder) {
        OAuth2AuthorizationRequest authorizationRequest = builder.build();
        Object registrationId = authorizationRequest.getAttribute(OAuth2ParameterNames.REGISTRATION_ID);
        Consumer<OAuth2AuthorizationRequest.Builder> consumer = authorizationRequestCustomizers.get(registrationId.toString());
        if (consumer != null) {
            consumer.accept(builder);
        }
    }
}
