/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.security.oauth2.client;

import cn.hutool.core.util.IdUtil;
import com.fanmu.muwu.auth.manager.UserManager;
import com.fanmu.muwu.common.security.SecurityConstants;
import com.fanmu.muwu.service.provider.user.api.model.dto.socialuser.SocialUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 认证成功处理器.
 */
@Component("oauth2AuthenticationSuccessHandler")
@Slf4j
public class Oauth2AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    UserManager userManager;

    /**
     * On authentication success.
     *
     * @param request        the request
     * @param response       the response
     * @param authentication the authentication
     * @throws IOException the io exception
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        logger.info("登录成功");
//		super.onAuthenticationSuccess(request, response, authentication);
        OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) authentication;
        String clientId = authenticationToken.getAuthorizedClientRegistrationId();
        OAuth2User auth2User = authenticationToken.getPrincipal();
        SocialUserInfo socialUserInfo = userManager.getSocialUserInfo(clientId, auth2User.getName());
        if (socialUserInfo == null) {
            socialUserInfo = userManager.addSocialUser(clientId, auth2User);
        }

        clearAuthenticationAttributes(request);

        String uuid = IdUtil.fastSimpleUUID();

        request.getSession().setAttribute(SecurityConstants.DEFAULT_PARAMETER_NAME_OAUTH2_PREFIX + uuid, socialUserInfo.getUserInfo().getUsername());
        request.getSession().setAttribute(SecurityConstants.LOGIN_TYPE, authenticationToken.getAuthorizedClientRegistrationId());
        String targetUrl = "/#/login?_2lBepC=" + uuid;
        response.sendRedirect(targetUrl);
    }

}
