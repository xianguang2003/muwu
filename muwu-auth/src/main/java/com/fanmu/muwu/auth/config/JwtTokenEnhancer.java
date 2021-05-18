/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;


/**
 * The class Token jwt enhancer.
 *
 */
public class JwtTokenEnhancer implements TokenEnhancer {

    @Autowired
    ObjectMapper objectMapper;

    /**
     * Enhance o auth 2 access token.
     *
     * @param accessToken          the access token
     * @param oAuth2Authentication the o auth 2 authentication
     *
     * @return the o auth 2 access token
     */
    @SneakyThrows
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication oAuth2Authentication) {
        Map<String, Object> info = new HashMap<>(8);
        info.put("timestamp", System.currentTimeMillis());
        Authentication authentication = oAuth2Authentication.getUserAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            Object principal = authentication.getPrincipal();
            info.put("principal", objectMapper.writeValueAsString(principal));
        }
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
        return accessToken;
    }

}
