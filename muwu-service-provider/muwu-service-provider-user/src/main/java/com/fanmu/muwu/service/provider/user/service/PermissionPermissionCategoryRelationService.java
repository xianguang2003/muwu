/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.service;

import com.fanmu.muwu.service.provider.user.api.model.dto.permission.PermissionInfo;
import com.fanmu.muwu.service.provider.user.api.model.dto.permissioncategory.PermissionCategoryInfo;
import com.fanmu.muwu.service.provider.user.model.domain.PermissionPermissionCategoryRelation;
import com.fanmu.muwu.common.coer.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 权限类别权限关联 服务类
 * </p>
 *
 * @author mumu
 * @since 2021-03-24
 */
public interface PermissionPermissionCategoryRelationService extends IService<PermissionPermissionCategoryRelation> {

    void addPermissionPermissionCategory(List<PermissionCategoryInfo> permissionCategoryInfos, Long permissionId);

}
