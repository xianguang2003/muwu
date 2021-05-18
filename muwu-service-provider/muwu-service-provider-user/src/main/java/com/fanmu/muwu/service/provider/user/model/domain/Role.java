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
 * 角色信息
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mw_role")
public class Role extends BaseModel {

    private static final long serialVersionUID=1L;

    /**
     * 角色名称
     */
    @TableField("role_name")
    private String roleName;

    /**
     * 角色编码
     */
    @TableField("role_code")
    private String roleCode;

    /**
     * 状态，0可用、1不可用
     */
    @TableField("status")
    private Integer status;

    /**
     * 描述
     */
    @TableField("remark")
    private String remark;


}
