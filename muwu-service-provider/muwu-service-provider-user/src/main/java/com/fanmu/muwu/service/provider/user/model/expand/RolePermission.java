/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.model.expand;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 角色权限信息
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
@Data
@EqualsAndHashCode()
public class RolePermission {

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 菜单权限地址
     */
    private List<String> menuUrl;

    /**
     * 页面元素权限地址
     */
    private List<String> elementUrl;

    /**
     * 权限地址
     */
    private List<String> permissionUrl;

    /**
     * 菜单权限权限地址
     */
    private List<String> menuPermissionUrl;

    /**
     * 页面权限权限地址
     */
    private List<String> elementPermissionUrl;
}
