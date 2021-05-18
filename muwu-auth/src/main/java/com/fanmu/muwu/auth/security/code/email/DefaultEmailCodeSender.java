/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.security.code.email;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认的短信验证码发送器
 *
 */
@Slf4j
public class DefaultEmailCodeSender implements EmailCodeSender {
	/**
	 * Send.
	 *
	 * @param email the mobile
	 * @param code  the code
	 */
	@Override
	public void send(String email, String code) {
		log.warn("请配置真实的邮件验证码发送器(SmsCodeSender)");
		log.info("向邮件" + email + "发送短信验证码" + code);
	}

}
