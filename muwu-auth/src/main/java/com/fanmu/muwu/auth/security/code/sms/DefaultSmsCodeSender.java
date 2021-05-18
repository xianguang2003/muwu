/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.security.code.sms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 默认的短信验证码发送器
 *
 */
@Slf4j
@Component
public class DefaultSmsCodeSender implements SmsCodeSender {
	/**
	 * Send.
	 *
	 * @param mobile the mobile
	 * @param code  the code
	 * @param ip  the ip
	 */
	@Override
	public void send(String mobile, String code, String ip) {
		log.info("ip地址:{}向手机: {}发送短信验证码:{}", ip, mobile, code);
	}

}
