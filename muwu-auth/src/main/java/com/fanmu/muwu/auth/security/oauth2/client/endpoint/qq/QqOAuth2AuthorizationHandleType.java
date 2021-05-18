/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.security.oauth2.client.endpoint.qq;

import com.fanmu.muwu.auth.security.oauth2.client.OAuth2AuthorizationHandleType;

public class QqOAuth2AuthorizationHandleType implements OAuth2AuthorizationHandleType {

    @Override
    public String getHandleType() {
        return "qq";
    }
}
