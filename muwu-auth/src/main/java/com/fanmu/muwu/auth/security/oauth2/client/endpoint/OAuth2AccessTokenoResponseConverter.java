/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.security.oauth2.client.endpoint;

import com.fanmu.muwu.auth.security.oauth2.client.OAuth2AuthorizationHandleType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;

public interface OAuth2AccessTokenoResponseConverter extends OAuth2AuthorizationHandleType, Converter<OAuth2AccessTokenoOriginalResponse, OAuth2AccessTokenResponse> {

}
