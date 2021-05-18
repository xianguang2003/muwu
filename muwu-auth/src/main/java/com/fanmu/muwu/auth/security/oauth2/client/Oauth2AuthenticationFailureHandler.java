/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.security.oauth2.client;

import com.fanmu.muwu.common.security.SecurityConstants;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败处理器
 *
 */
@Component("oauth2AuthenticationFailureHandler")
public class Oauth2AuthenticationFailureHandler extends ExceptionMappingAuthenticationFailureHandler {

	/**
	 * On authentication failure.
	 *
	 * @param request   the request
	 * @param response  the response
	 * @param exception the exception
	 *
	 * @throws IOException      the io exception
	 * @throws ServletException the servlet exception
	 */
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

		logger.info("登录失败");

		//FIXME 记录失败次数 和原因 ip等信息 5次登录失败,锁定用户, 不允许再次登录

		String targetUrl = SecurityConstants.DEFAULT_UNAUTHENTICATION_URL + "?errorMsg=" + exception.toString();;
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}
}
