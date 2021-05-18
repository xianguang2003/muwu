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
 * 权限类别
 * </p>
 *
 * @author mumu
 * @since 2021-03-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mw_permission_category")
public class PermissionCategory extends BaseModel {

    private static final long serialVersionUID=1L;

    /**
     * 名称
     */
    @TableField("category_name")
    private String categoryName;


}
