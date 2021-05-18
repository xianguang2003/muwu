/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.model.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户组角色关联
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
@Data
@EqualsAndHashCode()
@TableName("mw_user_group_role_relation")
public class UserGroupRoleRelation {

    private static final long serialVersionUID=1L;

    /**
     * 用户组id
     */
    @TableField("group_id")
    private Long groupId;

    /**
     * 角色id
     */
    @TableField("role_id")
    private Long roleId;


}
