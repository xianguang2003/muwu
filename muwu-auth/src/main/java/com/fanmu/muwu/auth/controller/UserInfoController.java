/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.controller;

import com.fanmu.muwu.common.config.properties.MuWuProperties;
import com.fanmu.muwu.common.web.extension.wrapper.WrapMapper;
import com.fanmu.muwu.common.web.extension.wrapper.Wrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class UserInfoController {

    @Autowired
    private MuWuProperties muWuProperties;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private ObjectMapper objectMapper;

    @ResponseBody
    @RequestMapping(value = "/auth/getUserInfo", method = RequestMethod.POST)
    public Wrapper getUserInfo() throws JsonProcessingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ("jwt".equals(muWuProperties.getSecurity().getOauth2().getTokenStore())) {
            if (authentication.getDetails() instanceof OAuth2AuthenticationDetails) {
                OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
                String tokenValue = details.getTokenValue();
                OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(tokenValue);
                Map<String, Object> additionalInformation = oAuth2AccessToken.getAdditionalInformation();
                Object principal = additionalInformation.get("principal");
                if (principal != null) {
                    Map value = objectMapper.readValue(principal.toString(), Map.class);
                    return WrapMapper.ok(value);
                }
            }
        }
        return WrapMapper.ok(authentication.getPrincipal());
    }
}
