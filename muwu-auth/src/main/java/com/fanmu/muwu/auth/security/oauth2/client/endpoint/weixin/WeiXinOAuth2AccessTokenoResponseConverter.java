/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.security.oauth2.client.endpoint.weixin;

import com.fanmu.muwu.auth.security.oauth2.client.endpoint.OAuth2AccessTokenoOriginalResponse;
import com.fanmu.muwu.auth.security.oauth2.client.endpoint.OAuth2AccessTokenoResponseConverter;
import com.google.common.collect.Sets;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class WeiXinOAuth2AccessTokenoResponseConverter extends WeiXinOAuth2AuthorizationHandleType implements OAuth2AccessTokenoResponseConverter {

    @Override
    public OAuth2AccessTokenResponse convert(OAuth2AccessTokenoOriginalResponse tokenoOriginalResponse) {
        Map<String, Object> response = tokenoOriginalResponse.getResponse();
        OAuth2AccessTokenResponse.Builder builder =
                OAuth2AccessTokenResponse.withToken(response.get(OAuth2ParameterNames.ACCESS_TOKEN).toString())
                        .expiresIn(Long.parseLong(response.get(OAuth2ParameterNames.EXPIRES_IN).toString()))
                        .refreshToken(response.get(OAuth2ParameterNames.REFRESH_TOKEN).toString())
                        .scopes(Sets.newHashSet(response.get(OAuth2ParameterNames.SCOPE).toString()))
                        .tokenType(OAuth2AccessToken.TokenType.BEARER)
                        .additionalParameters(response);
        return builder.build();
    }
}
