/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.web.api.config;

import org.springframework.security.core.AuthenticationException;

public class PlatformAuthenticationException extends AuthenticationException {

    public PlatformAuthenticationException(String msg) {
        super(msg);
    }

}
