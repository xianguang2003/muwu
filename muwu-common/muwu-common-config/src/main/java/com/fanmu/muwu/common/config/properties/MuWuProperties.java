/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.common.config.properties;

import com.fanmu.muwu.common.base.constant.GlobalConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = GlobalConstant.ROOT_PREFIX)
public class MuWuProperties {

    private SecurityProperties security = new SecurityProperties();
    private SwaggerProperties swagger = new SwaggerProperties();
    private OssProperties oss = new OssProperties();

}
