/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.security.code;

import com.fanmu.muwu.common.config.properties.MuWuProperties;
import com.fanmu.muwu.auth.security.code.email.DefaultEmailCodeSender;
import com.fanmu.muwu.auth.security.code.email.EmailCodeSender;
import com.fanmu.muwu.auth.security.code.image.ImageCodeGenerator;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 验证码相关的扩展点配置。配置在这里的bean，业务系统都可以通过声明同类型或同名的bean来覆盖安全
 * 模块默认的配置。
 *
 */
@Configuration
public class ValidateCodeBeanConfig {

	@Autowired
	private MuWuProperties muWuProperties;
	@Autowired
	private Producer captchaProducer;

	/**
	 * 图片验证码图片生成器
	 *
	 * @return validate code generator
	 */
	@Bean
	@ConditionalOnMissingBean(name = "imageValidateCodeGenerator")
	public ValidateCodeGenerator imageValidateCodeGenerator() {
		ImageCodeGenerator codeGenerator = new ImageCodeGenerator();
		codeGenerator.setSecurityProperties(muWuProperties.getSecurity());
		codeGenerator.setCaptchaProducer(captchaProducer);
		return codeGenerator;
	}

	/**
	 * 邮箱验证码发送器
	 *
	 * @return sms code sender
	 */
	@Bean
	@ConditionalOnMissingBean(EmailCodeSender.class)
	public EmailCodeSender emailCodeSender() {
		return new DefaultEmailCodeSender();
	}

}
