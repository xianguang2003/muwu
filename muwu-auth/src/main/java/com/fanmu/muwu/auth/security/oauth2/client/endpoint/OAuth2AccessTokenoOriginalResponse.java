/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.security.oauth2.client.endpoint;


import lombok.Data;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;

import java.util.Map;

@Data
public class OAuth2AccessTokenoOriginalResponse {

    private Map<String, Object> response;

    private OAuth2AuthorizationCodeGrantRequest oAuth2AuthorizationCodeGrantRequest;

}
