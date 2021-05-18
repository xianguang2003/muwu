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
 * 页面元素权限关联
 * </p>
 *
 * @author mumu
 * @since 2021-03-29
 */
@Data
@EqualsAndHashCode()
@TableName("mw_element_permission_relation")
public class ElementPermissionRelation {

    private static final long serialVersionUID=1L;

    /**
     * 页面元素ID
     */
    @TableField("element_id")
    private Long elementId;

    /**
     * 权限ID
     */
    @TableField("permission_id")
    private Long permissionId;


}
