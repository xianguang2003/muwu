/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.api.model.dto.permission;

import com.fanmu.muwu.service.provider.user.api.model.dto.permissioncategory.PermissionCategoryInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 权限
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PermissionInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 权限名称
     */
    private String permissionName;

    /**
     * 权限编码
     */
    private String permissionCode;

    /**
     * 拦截URL前缀
     */
    private String url;

    /**
     * 状态，0可用、1不可用
     */
    private Integer status;

    /**
     * 白名单，0需要权限验证、1不需要权限验证
     */
    private Integer whitelist;

    /**
     * 角色共享权限，0不是、1是
     */
    private Integer roleSharePermission;

    /**
     * 描述
     */
    private String remark;

    /**
     * 权限分类
     */
    private List<PermissionCategoryInfo> permissionCategoryInfos;
}
