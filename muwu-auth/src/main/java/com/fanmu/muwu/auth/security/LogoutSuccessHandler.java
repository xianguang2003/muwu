/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.security;

import com.fanmu.muwu.common.web.extension.wrapper.WrapMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 认证失败处理器
 */
@Component("logoutSuccessHandler")
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    @Resource
    private ObjectMapper objectMapper;

    @Lazy
    @Resource
    private ConsumerTokenServices consumerTokenServices;

    private TokenExtractor tokenExtractor = new BearerTokenExtractor();

    /**
     * On authentication failure.
     *
     * @param request        the request
     * @param response       the response
     * @param authentication the authentication
     * @throws IOException      the io exception
     * @throws ServletException the servlet exception
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        Authentication oauthAuthentication = tokenExtractor.extract(request);
        if (oauthAuthentication != null) {
            Object principal = oauthAuthentication.getPrincipal();
            if (!Objects.isNull(principal)) {
                boolean revokeTokenBool = consumerTokenServices.revokeToken(principal.toString());
                if (revokeTokenBool) {
                    response.getWriter().write(objectMapper.writeValueAsString(WrapMapper.wrap(200, "退出成功")));
                    return;
                }
            }
        }
        response.getWriter().write(objectMapper.writeValueAsString(WrapMapper.error("退出失败")));
    }
}
