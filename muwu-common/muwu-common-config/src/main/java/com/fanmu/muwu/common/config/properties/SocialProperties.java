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
 * 社交登录配置项
 *
 */
@Data
public class SocialProperties {

	/**
	 * 社交登录功能拦截的url
	 */
	private String filterProcessesUrl = "/socialLogin";

	private QQProperties qq = new QQProperties();

	private WeixinProperties weixin = new WeixinProperties();

}
