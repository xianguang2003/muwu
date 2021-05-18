/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.security.authentication.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * oauth2社交登录配置
 *
 */
@Component
public class SocialCodeAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler;
	@Autowired
	private AuthenticationFailureHandler authenticationFailureHandler;
	@Autowired
	private UserDetailsService userDetailsService;

	/**
	 * Configure.
	 *
	 * @param http the http
	 */
	@Override
	public void configure(HttpSecurity http) {

		SocialCodeAuthenticationFilter socialCodeAuthenticationFilter = new SocialCodeAuthenticationFilter();
		socialCodeAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
		socialCodeAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
		socialCodeAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);

		SocialCodeAuthenticationProvider socialCodeAuthenticationProvider = new SocialCodeAuthenticationProvider();
		socialCodeAuthenticationProvider.setUserDetailsService(userDetailsService);

		http.authenticationProvider(socialCodeAuthenticationProvider)
				.addFilterAfter(socialCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	}

}
