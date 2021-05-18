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
 * 菜单
 * </p>
 *
 * @author mumu
 * @since 2021-04-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mw_menu")
public class Menu extends BaseModel {

    private static final long serialVersionUID=1L;

    /**
     * 菜单名称
     */
    @TableField("menu_name")
    private String menuName;

    /**
     * 菜单编码
     */
    @TableField("menu_code")
    private String menuCode;

    /**
     * 菜单URL
     */
    @TableField("url")
    private String url;

    /**
     * 组件路径
     */
    @TableField("component")
    private String component;

    /**
     * 父菜单ID
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 序号
     */
    @TableField("number")
    private Integer number;

    /**
     * 状态，0可用、1不可用
     */
    @TableField("status")
    private Integer status;

    /**
     * 0不隐藏、1隐藏
     */
    @TableField("hidden")
    private Integer hidden;

    /**
     * 描述
     */
    @TableField("remark")
    private String remark;

    /**
     * 图标
     */
    @TableField("icon")
    private String icon;

    /**
     * 层级(最多三级1,2,3)
     */
    @TableField("level")
    private Integer level;

    /**
     * 是否叶子节点,0不是1是
     */
    @TableField("leaf")
    private Boolean leaf;


}
