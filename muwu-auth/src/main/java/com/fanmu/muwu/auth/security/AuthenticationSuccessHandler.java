/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.security;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.fanmu.muwu.auth.enums.LoginTypeEnum;
import com.fanmu.muwu.auth.manager.UserManager;
import com.fanmu.muwu.auth.model.dto.AuthLog;
import com.fanmu.muwu.common.security.SecurityConstants;
import com.fanmu.muwu.common.util.RequestUtil;
import com.fanmu.muwu.common.web.extension.wrapper.WrapMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;


/**
 * 认证成功处理器.
 */
@Component("authenticationSuccessHandler")
@Slf4j
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Resource
    private ObjectMapper objectMapper;

    @Lazy
    @Resource
    private ClientDetailsService clientDetailsService;

    @Lazy
    @Resource
    private AuthorizationServerTokenServices authorizationServerTokenServices;

    @Lazy
    @Resource
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserManager userManager;

    private static final String BEARER_TOKEN_TYPE = "Basic ";

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
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith(BEARER_TOKEN_TYPE)) {
            throw new UnapprovedClientAuthenticationException("请求头中无client信息");
        }

        String[] tokens = RequestUtil.extractAndDecodeHeader(header);
        assert tokens.length == 2;

        //获取clientId 和 clientSecret
        String clientId = tokens[0];
        String clientSecret = tokens[1];

        //获取 ClientDetails
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
//		PasswordEncoder passwordEncoder = SpringContextHolder.getBean(PasswordEncoder.class);
        if (clientDetails == null) {
            throw new UnapprovedClientAuthenticationException("clientId对应的配置信息不存在:" + clientId);
		/*} else if (!StringUtils.equals(clientDetails.getClientSecret(), clientSecret)) {
			throw new UnapprovedClientAuthenticationException("clientSecret不匹配:" + clientId);
		}*/
        } else if (!passwordEncoder.matches(clientSecret, clientDetails.getClientSecret())) {
            throw new UnapprovedClientAuthenticationException("clientSecret不匹配:" + clientId);
        }
        //密码授权 模式, 组建 authentication
        TokenRequest tokenRequest = new TokenRequest(Maps.newHashMap(), clientId, clientDetails.getScope(), "custom");

        OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);

//		AuthorizationServerTokenServices authorizationServerTokenServices = SpringContextHolder.getBean(AuthorizationServerTokenServices.class);
        OAuth2AccessToken token = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);

        clearAuthenticationAttributes(request);

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write((objectMapper.writeValueAsString(WrapMapper.ok(token))));

        // 记录登录日志
        HttpSession session = request.getSession(false);
        String loginType = LoginTypeEnum.ACCOUNT.getType();
        if (session != null) {
            Object loginTypeTmp = session.getAttribute(SecurityConstants.LOGIN_TYPE);
            if (loginTypeTmp != null) {
                loginType = loginTypeTmp.toString();
            }
        }
        String uaStr = request.getHeader("user-agent");
        UserAgent userAgent = UserAgentUtil.parse(uaStr);
        AuthLog authLog = new AuthLog();
        authLog.setLoginType(loginType);
        authLog.setType("登录");
        authLog.setUsername(authentication.getName());
        authLog.setOs(userAgent.getOs().getName());
        authLog.setBrowser(userAgent.getBrowser().getName());
        authLog.setIp(RequestUtil.getRemoteAddr(request));
        authLog.setTerminal(userAgent.isMobile() ? "mobile" : "pc");
        authLog.setStatus(0);
        authLog.setDescription("登录成功");
        authLog.setLoginTime(new Date());
        userManager.saveLoginLog(authLog);
    }

}
