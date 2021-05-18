/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.api.model.dto.user;

import com.fanmu.muwu.service.provider.user.api.model.dto.role.RoleInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 用户信息
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 姓名
     */
    private String username;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 用户性别（0未知 1男 2女）
     */
    private Integer sex;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邮件地址
     */
    private String email;

    /**
     * 状态，0可用、1不可用
     */
    private Integer status;

    /**
     * 数据来源
     */
    private String source;

    /**
     * 角色信息
     */
    private List<RoleInfo> roleInfos;
}
