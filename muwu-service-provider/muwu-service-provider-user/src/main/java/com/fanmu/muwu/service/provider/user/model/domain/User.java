/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.model.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fanmu.muwu.common.coer.extension.model.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户信息
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mw_user")
public class User extends BaseModel {

    private static final long serialVersionUID=1L;

    /**
     * 姓名
     */
    @TableField("username")
    private String username;

    /**
     * 登录密码
     */
    @TableField("password")
    private String password;

    /**
     * 昵称
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 用户性别（0未知 1男 2女）
     */
    @TableField("sex")
    private Integer sex;

    /**
     * 手机号
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 邮件地址
     */
    @TableField("email")
    private String email;

    /**
     * 状态，0可用、1不可用
     */
    @TableField("status")
    private Integer status;

    /**
     * 数据来源
     */
    @TableField("source")
    private String source;


}
