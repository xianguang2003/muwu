/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.common.config.properties;

import lombok.Data;

/**
 * The class O auth 2 properties.
 *
 */
@Data
public class OAuth2Properties {

	/**
	 * 使用jwt时为token签名的秘钥
	 */
	private String jwtSigningKey;

	/**
	 * 使用redis、jwt作为认证原
	 */
	private String tokenStore;

	/**
	 * 客户端配置
	 */
	private OAuth2ClientProperties[] clients = {};

	/**
	 * 鉴权忽略的配置
	 */
	private IgnoreSecurityProperties ignore = new IgnoreSecurityProperties();

}
