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
import java.util.Date;

@Data
public class LoginLogInfo implements Serializable {

    private String id;

    private String loginType;

    private String type;

    private String username;

    private String os;

    private String browser;

    private String ip;

    private String terminal;

    private String country;

    private String province;

    private String city;

    private String location;

    private Integer status;

    private String description;

    private Date loginTime;
}
