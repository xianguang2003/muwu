/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.security.authentication.social;

import com.fanmu.muwu.common.security.SecurityConstants;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * oauth2社交登录过滤器
 *
 */
public class SocialCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	private static final String POST = "POST";
	// ~ Static fields/initializers
	// =====================================================================================

	private String principalParameter = SecurityConstants.DEFAULT_PARAMETER_NAME_PRINCIPAL;
	private boolean postOnly = true;

	// ~ Constructors
	// ===================================================================================================

	/**
	 * Instantiates a new Sms code authentication filter.
	 */
	public SocialCodeAuthenticationFilter() {
		super(new AntPathRequestMatcher(SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_SOCIAL, POST));
	}

	// ~ Methods
	// ========================================================================================================

	/**
	 * Attempt authentication authentication.
	 *
	 * @param request  the request
	 * @param response the response
	 *
	 * @return the authentication
	 *
	 * @throws AuthenticationException the authentication exception
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		if (postOnly && !POST.equals(request.getMethod())) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}

		String principal = obtainPrincipal(request);

		if (principal == null) {
			principal = "";
		}

		principal = principal.trim();
		//
		Object attribute = request.getSession().getAttribute(SecurityConstants.DEFAULT_PARAMETER_NAME_OAUTH2_PREFIX + principal);
		if (attribute == null) {
			principal = "";
		} else {
			principal = attribute.toString();
		}

		SocialCodeAuthenticationToken authRequest = new SocialCodeAuthenticationToken(principal);

		// Allow subclasses to set the "details" property
		setDetails(request, authRequest);

		return this.getAuthenticationManager().authenticate(authRequest);
	}


	/**
	 * 获取认证标识
	 *
	 * @param request the request
	 *
	 * @return the string
	 */
	protected String obtainPrincipal(HttpServletRequest request) {
		return request.getParameter(principalParameter);
	}

	/**
	 * Provided so that subclasses may configure what is put into the
	 * authentication request's details property.
	 *
	 * @param request     that an authentication request is being created for
	 * @param authRequest the authentication request object that should have its details            set
	 */
	protected void setDetails(HttpServletRequest request, SocialCodeAuthenticationToken authRequest) {
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
	}

	/**
	 * Sets the parameter name which will be used to obtain the username from
	 * the login request.
	 *
	 * @param principalParameter the parameter name. Defaults to "username".
	 */
	public void setPrincipalParameter(String principalParameter) {
		Assert.hasText(principalParameter, "Username parameter must not be empty or null");
		this.principalParameter = principalParameter;
	}


	/**
	 * Defines whether only HTTP POST requests will be allowed by this filter.
	 * If set to true, and an authentication request is received which is not a
	 * POST request, an exception will be raised immediately and authentication
	 * will not be attempted. The <tt>unsuccessfulAuthentication()</tt> method
	 * will be called as if handling a failed authentication.
	 * <p>
	 * Defaults to <tt>true</tt> but may be overridden by subclasses.
	 *
	 * @param postOnly the post only
	 */
	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}

	/**
	 * Gets principal parameter.
	 *
	 * @return the principal parameter
	 */
	public final String getPrincipalParameter() {
		return principalParameter;
	}

}
