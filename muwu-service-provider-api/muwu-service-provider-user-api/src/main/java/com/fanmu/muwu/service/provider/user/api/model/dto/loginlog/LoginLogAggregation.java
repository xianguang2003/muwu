/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.api.model.dto.loginlog;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginLogAggregation implements Serializable {

    String name;

    Long value;

}
