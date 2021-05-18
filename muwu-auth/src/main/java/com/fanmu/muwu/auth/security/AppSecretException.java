/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.security;

/**
 * The class App secret exception.
 *
 */
public class AppSecretException extends RuntimeException {

	private static final long serialVersionUID = -1629364510827838114L;

	/**
	 * Instantiates a new App secret exception.
	 *
	 * @param msg the msg
	 */
	public AppSecretException(String msg) {
		super(msg);
	}

}
