/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.security.oauth2.client.endpoint.weixin;

import com.fanmu.muwu.auth.security.oauth2.client.endpoint.OAuth2AuthorizationRequestCustomizer;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;

@Component
public class WeiXinOAuth2AuthorizationRequestCustomizer extends WeiXinOAuth2AuthorizationHandleType implements OAuth2AuthorizationRequestCustomizer {

    @Override
    public void accept(OAuth2AuthorizationRequest.Builder builder) {
        builder.parameters(params -> {
            params.put("appid", params.get(OAuth2ParameterNames.CLIENT_ID));
            params.put("scope", "snsapi_login");
            params.remove("client_id");
        });
    }
}
