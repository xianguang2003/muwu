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
 * 校验码存取器
 *
 */
public interface ValidateCodeRepository {

	/**
	 * 保存验证码
	 *
	 * @param request          the request
	 * @param code             the code
	 * @param validateCodeType the validate code type
	 */
	void save(ServletWebRequest request, ValidateCode code, ValidateCodeType validateCodeType);

	/**
	 * 获取验证码
	 *
	 * @param request          the request
	 * @param validateCodeType the validate code type
	 *
	 * @return validate code
	 */
	ValidateCode get(ServletWebRequest request, ValidateCodeType validateCodeType);

	/**
	 * 移除验证码
	 *
	 * @param request  the request
	 * @param codeType the code type
	 */
	void remove(ServletWebRequest request, ValidateCodeType codeType);

}
