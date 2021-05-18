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
 * 用户组
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mw_user_group")
public class UserGroup extends BaseModel {

    private static final long serialVersionUID=1L;

    /**
     * 用户组名称
     */
    @TableField("group_name")
    private String groupName;

    /**
     * 用户组编码
     */
    @TableField("group_code")
    private String groupCode;

    /**
     * 父级id
     */
    @TableField("parent_id")
    private Long parentId;

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
