/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.service;

import com.fanmu.muwu.service.provider.user.api.model.dto.role.RoleInfo;
import com.fanmu.muwu.service.provider.user.model.domain.UserRoleRelation;
import com.fanmu.muwu.common.coer.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户角色关联 服务类
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
public interface UserRoleRelationService extends IService<UserRoleRelation> {

    void addUserRole(List<RoleInfo> roleInfos, Long userId);

}
