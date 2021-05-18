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
 * The class Sms code properties.
 *
 */
@Data
public class SmsCodeProperties {

	/**
	 * 验证码长度
	 */
	private int length = 6;
	/**
	 * 过期时间
	 */
	private int expireIn = 60;
	/**
	 * 要拦截的url，多个url用逗号隔开，ant pattern
	 */
	private String url;
	/**
	 * 每天每个手机号最大送送短信数量
	 */
	private int mobileMaxSendCount;
	/**
	 * 每天每个IP最大送送短信数量
	 */
	private int ipMaxSendCount;
	/**
	 * 每天最大送送短信数量
	 */
	private int totalMaxSendCount;


}
