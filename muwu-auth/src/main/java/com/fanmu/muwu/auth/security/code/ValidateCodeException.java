/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.security.code;

import org.springframework.security.core.AuthenticationException;


/**
 * The class Validate code exception.
 *
 */
public class ValidateCodeException extends AuthenticationException {


	private static final long serialVersionUID = -7285211528095468156L;

	/**
	 * Instantiates a new Validate code exception.
	 *
	 * @param msg the msg
	 */
	public ValidateCodeException(String msg) {
		super(msg);
	}

}
