/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.security.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 校验码处理器，封装不同校验码的处理逻辑
 *
 */
public interface ValidateCodeProcessor {

	/**
	 * 创建校验码
	 *
	 * @param request the request
	 *
	 * @throws Exception the exception
	 */
	void create(ServletWebRequest request) throws Exception;

	/**
	 * 校验验证码(验证后删除)
	 *
	 * @param servletWebRequest the servlet web request
	 */
	void validate(ServletWebRequest servletWebRequest);

	/**
	 * 校验验证码(验证后不删除)
	 *
	 * @param servletWebRequest the servlet web request
	 */
	void check(ServletWebRequest servletWebRequest);

}
