/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class AuthLog {

    private String loginType;

    private String type;

    private String username;

    private String os;

    private String browser;

    private String ip;

    private String terminal;

    private Integer status;

    private String description;

    private Date loginTime;
}
