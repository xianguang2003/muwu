/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.api.service;

import com.fanmu.muwu.common.base.pojo.BaseDTO;
import com.fanmu.muwu.service.provider.user.api.model.dto.permission.PermissionInfo;
import com.fanmu.muwu.service.provider.user.api.model.query.permission.QueryPermission;

import java.util.List;

/**
 * <p>
 * 权限 rpc服务类
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
public interface PermissionRpcApi {

    void insertPermission(PermissionInfo permissionInfo);

    void deletePermissionById(Long id);

    void updatePermission(PermissionInfo permissionInfo);

    PermissionInfo getPermissionById(Long id);

    List<PermissionInfo> listPermission();

    List<PermissionInfo> listPermission(QueryPermission queryPermission);

    BaseDTO<PermissionInfo> listPermissionPage(QueryPermission queryPermission);

    void refreshCache();

    void batchUpdateWhitelist(List<PermissionInfo> permissionInfos);

    void batchUpdateRoleSharePermission(List<PermissionInfo> permissionInfos);
}
