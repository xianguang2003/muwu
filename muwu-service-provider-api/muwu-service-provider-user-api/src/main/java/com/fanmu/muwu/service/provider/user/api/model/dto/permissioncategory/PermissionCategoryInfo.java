/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.api.model.dto.permissioncategory;

import com.fanmu.muwu.service.provider.user.api.model.dto.permission.PermissionInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 权限类别
 * </p>
 *
 * @author mumu
 * @since 2021-03-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PermissionCategoryInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 名称
     */
    private String categoryName;

    /**
     * 权限
     */
    private List<PermissionInfo> permissionInfos;
}
