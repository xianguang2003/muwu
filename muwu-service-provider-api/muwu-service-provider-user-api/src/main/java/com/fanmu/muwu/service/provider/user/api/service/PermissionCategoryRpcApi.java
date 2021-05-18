/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.api.service;

import com.fanmu.muwu.common.base.pojo.BaseDTO;
import com.fanmu.muwu.service.provider.user.api.model.dto.permissioncategory.PermissionCategoryInfo;
import com.fanmu.muwu.service.provider.user.api.model.query.permissioncategory.QueryPermissionCategory;

import java.util.List;
/**
 * <p>
 * 权限类别 rpc服务类
 * </p>
 *
 * @author mumu
 * @since 2021-03-24
 */
public interface PermissionCategoryRpcApi {

    void insertPermissionCategory(PermissionCategoryInfo permissionCategoryInfo);

    void deletePermissionCategoryById(Long id);

    void updatePermissionCategory(PermissionCategoryInfo permissionCategoryInfo);

    PermissionCategoryInfo getPermissionCategoryById(Long id);

    List<PermissionCategoryInfo> listPermissionCategory();

    List<PermissionCategoryInfo> listPermissionCategory(QueryPermissionCategory queryPermissionCategory);

    BaseDTO<PermissionCategoryInfo> listPermissionCategoryPage(QueryPermissionCategory queryPermissionCategory);

    void generatePermission(List<PermissionCategoryInfo> permissionCategoryInfos);
}
