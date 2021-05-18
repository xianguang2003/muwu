/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.security.code.sms;

import com.fanmu.muwu.common.config.properties.MuWuProperties;
import com.fanmu.muwu.auth.security.code.ValidateCode;
import com.fanmu.muwu.auth.security.code.ValidateCodeGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import javax.annotation.Resource;

/**
 * 短信验证码生成器
 *
 */
@Component("smsValidateCodeGenerator")
public class SmsCodeGenerator implements ValidateCodeGenerator {

	@Resource
	private MuWuProperties muWuProperties;

	/**
	 * Generate validate code.
	 *
	 * @param request the request
	 *
	 * @return the validate code
	 */
	@Override
	public ValidateCode generate(ServletWebRequest request) {
		String code = RandomStringUtils.randomNumeric(muWuProperties.getSecurity().getCode().getSms().getLength());
		return new ValidateCode(code, muWuProperties.getSecurity().getCode().getSms().getExpireIn());
	}
}
