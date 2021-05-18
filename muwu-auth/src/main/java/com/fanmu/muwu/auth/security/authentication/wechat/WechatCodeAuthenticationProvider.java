/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.security.authentication.wechat;

import com.fanmu.muwu.auth.security.ExtensionUserDetailsService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 短信登录验证逻辑
 * <p>
 * 由于短信验证码的验证在过滤器里已完成，这里直接读取用户信息即可。
 *
 */
public class WechatCodeAuthenticationProvider implements AuthenticationProvider {

	private ExtensionUserDetailsService extensionUserDetailsService;

	/**
	 * Authenticate authentication.
	 *
	 * @param authentication the authentication
	 *
	 * @return the authentication
	 *
	 * @throws AuthenticationException the authentication exception
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		WechatCodeAuthenticationToken authenticationToken = (WechatCodeAuthenticationToken) authentication;

		UserDetails user = extensionUserDetailsService.loadUserByWechatCode(authenticationToken.getAppId(), (String) authenticationToken.getPrincipal());

		if (user == null) {
			throw new InternalAuthenticationServiceException("无法获取用户信息");
		}

		WechatCodeAuthenticationToken authenticationResult = new WechatCodeAuthenticationToken(user, user.getAuthorities());

		authenticationResult.setDetails(authenticationToken.getDetails());

		return authenticationResult;
	}

	/**
	 * Supports boolean.
	 *
	 * @param authentication the authentication
	 *
	 * @return the boolean
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return WechatCodeAuthenticationToken.class.isAssignableFrom(authentication);
	}

	/**
	 * Gets user details service.
	 *
	 * @return the user details service
	 */
	public ExtensionUserDetailsService getExtensionUserDetailsService() {
		return extensionUserDetailsService;
	}

	/**
	 * Sets user details service.
	 *
	 * @param extensionUserDetailsService the user details service
	 */
	public void setExtensionUserDetailsService(ExtensionUserDetailsService extensionUserDetailsService) {
		this.extensionUserDetailsService = extensionUserDetailsService;
	}
}
