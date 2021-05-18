/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.api.model.dto.menu;

import com.fanmu.muwu.service.provider.user.api.model.dto.permission.PermissionInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 菜单
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MenuInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单编码
     */
    private String menuCode;

    /**
     * 菜单URL
     */
    private String url;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 序号
     */
    private Integer number;

    /**
     * 状态，0可用、1不可用
     */
    private Integer status;

    /**
     * 0不隐藏、1隐藏
     */
    private Integer hidden;

    /**
     * 描述
     */
    private String remark;

    /**
     * 图标
     */
    private String icon;

    /**
     * 层级(最多三级1,2,3)
     */
    private Integer level;

    /**
     * 是否叶子节点,0不是1是
     */
    private Boolean leaf;

    /**
     * 权限
     */
    private List<PermissionInfo> permissionInfos;
}
