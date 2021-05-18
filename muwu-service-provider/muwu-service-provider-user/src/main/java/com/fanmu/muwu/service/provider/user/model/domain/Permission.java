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
 * 权限
 * </p>
 *
 * @author mumu
 * @since 2021-04-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mw_permission")
public class Permission extends BaseModel {

    private static final long serialVersionUID=1L;

    /**
     * 权限名称
     */
    @TableField("permission_name")
    private String permissionName;

    /**
     * 权限编码
     */
    @TableField("permission_code")
    private String permissionCode;

    /**
     * 拦截URL前缀
     */
    @TableField("url")
    private String url;

    /**
     * 状态，0可用、1不可用
     */
    @TableField("status")
    private Integer status;

    /**
     * 白名单，0需要权限验证、1不需要权限验证
     */
    @TableField("whitelist")
    private Integer whitelist;

    /**
     * 角色共享权限，0不是、1是
     */
    @TableField("role_share_permission")
    private Integer roleSharePermission;

    /**
     * 描述
     */
    @TableField("remark")
    private String remark;


}
