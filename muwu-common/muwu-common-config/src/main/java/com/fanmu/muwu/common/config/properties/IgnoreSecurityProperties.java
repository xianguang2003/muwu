/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.common.config.properties;

import lombok.Data;

import java.util.List;


/**
 * The type Ignore filter properties.
 */
@Data
public class IgnoreSecurityProperties {
    /**
     * 忽略的 URL 表达式
     */
    private List<String> urls;

    /**
     * 忽略的客户端 ID
     */
    private List<String> clientIds;
}
