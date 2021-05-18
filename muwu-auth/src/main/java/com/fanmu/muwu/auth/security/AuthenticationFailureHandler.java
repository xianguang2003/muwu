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
import com.fanmu.muwu.common.web.extension.wrapper.WrapMapper;
import com.fanmu.muwu.common.security.SecurityConstants;
import com.fanmu.muwu.common.util.RequestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

/**
 * 认证失败处理器
 *
 */
@Component("authenticationFailureHandler")
public class AuthenticationFailureHandler extends ExceptionMappingAuthenticationFailureHandler {

	@Resource
	private ObjectMapper objectMapper;

	@Autowired
	private UserManager userManager;

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

		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		response.getWriter().write(objectMapper.writeValueAsString(WrapMapper.error(exception.getMessage())));

		// 记录登录日志
		HttpSession session = request.getSession(false);
		String loginType = LoginTypeEnum.ACCOUNT.getType();
		if (session != null) {
			Object loginTypeTmp = session.getAttribute(SecurityConstants.LOGIN_TYPE);
			if (loginTypeTmp != null) {
				loginType = loginTypeTmp.toString();
			}
		}
		String username = "未知";
		if (loginType.equals(LoginTypeEnum.ACCOUNT.getType())) {
			username = request.getParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY);
		}
		String uaStr = request.getHeader("user-agent");
		UserAgent userAgent = UserAgentUtil.parse(uaStr);
		AuthLog authLog = new AuthLog();
		authLog.setLoginType(loginType);
		authLog.setType("登录");
		authLog.setUsername(username);
		authLog.setOs(userAgent.getOs().getName());
		authLog.setBrowser(userAgent.getBrowser().getName());
		authLog.setIp(RequestUtil.getRemoteAddr(request));
		authLog.setTerminal(userAgent.isMobile() ? "mobile" : "pc");
		authLog.setStatus(1);
		authLog.setDescription("登录失败");
		authLog.setLoginTime(new Date());
		userManager.saveLoginLog(authLog);
	}
}
