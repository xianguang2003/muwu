/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.api.service;

import com.fanmu.muwu.common.base.pojo.BaseDTO;
import com.fanmu.muwu.service.provider.user.api.model.dto.role.RoleInfo;
import com.fanmu.muwu.service.provider.user.api.model.query.role.QueryRole;

import java.util.List;
/**
 * <p>
 * 角色信息 rpc服务类
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
public interface RoleRpcApi {

    void insertRole(RoleInfo roleInfo);

    void deleteRoleById(Long id);

    void updateRole(RoleInfo roleInfo);

    RoleInfo getRoleById(Long id);

    List<RoleInfo> listRole();

    List<RoleInfo> listRole(QueryRole queryRole);

    BaseDTO<RoleInfo> listRolePage(QueryRole queryRole);

    void saveRoleMenu(RoleInfo roleInfo);

    void updateRolePermission(RoleInfo roleInfo);

    void refreshCache();
}
