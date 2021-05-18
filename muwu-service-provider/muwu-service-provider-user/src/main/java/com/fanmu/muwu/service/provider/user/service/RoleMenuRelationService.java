/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.service;

import com.fanmu.muwu.service.provider.user.api.model.dto.menu.MenuInfo;
import com.fanmu.muwu.service.provider.user.model.domain.RoleMenuRelation;
import com.fanmu.muwu.common.coer.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色菜单关联 服务类
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
public interface RoleMenuRelationService extends IService<RoleMenuRelation> {

    void addRoleMenu(List<MenuInfo> menuInfos, Long roleId);

}
