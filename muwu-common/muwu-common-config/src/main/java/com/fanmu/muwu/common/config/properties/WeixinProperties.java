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
 * 微信登录配置项
 *
 */
@Data
public class WeixinProperties {

    /**
     * 第三方id，用来决定发起第三方登录的url，默认是 weixin。
     */
    private String providerId = "weixin";
    /**
     * 第三方回调地址
     */
    private String callBackUrl;

    private String appId;

    private String appSecret;
}
