/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.security.authentication.wechat;

import com.fanmu.muwu.auth.security.ExtensionUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * 短信登录配置
 *
 */
@Component
public class WechatCodeAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler;
	@Autowired
	private AuthenticationFailureHandler authenticationFailureHandler;
	@Autowired
	private ExtensionUserDetailsService extensionUserDetailsService;

	/**
	 * Configure.
	 *
	 * @param http the http
	 */
	@Override
	public void configure(HttpSecurity http) {

		WechatCodeAuthenticationFilter wechatCodeAuthenticationFilter = new WechatCodeAuthenticationFilter();
		wechatCodeAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
		wechatCodeAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
		wechatCodeAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);

		WechatCodeAuthenticationProvider wechatCodeAuthenticationProvider = new WechatCodeAuthenticationProvider();
		wechatCodeAuthenticationProvider.setExtensionUserDetailsService(extensionUserDetailsService);

		http.authenticationProvider(wechatCodeAuthenticationProvider)
				.addFilterAfter(wechatCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	}

}
