/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.service.impl;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fanmu.muwu.service.provider.user.api.model.dto.menu.MenuInfo;
import com.fanmu.muwu.service.provider.user.api.model.dto.permission.PermissionInfo;
import com.fanmu.muwu.service.provider.user.model.domain.RoleMenuRelation;
import com.fanmu.muwu.service.provider.user.model.domain.RolePermissionRelation;
import com.fanmu.muwu.service.provider.user.mapper.RolePermissionRelationMapper;
import com.fanmu.muwu.service.provider.user.service.RolePermissionRelationService;
import com.fanmu.muwu.common.coer.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色权限关联 服务实现类
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
@Service
public class RolePermissionRelationServiceImpl extends ServiceImpl<RolePermissionRelationMapper, RolePermissionRelation> implements RolePermissionRelationService {

    @Override
    public void addRolePermission(List<PermissionInfo> permissionInfos, Long roleId) {
        super.remove(new QueryWrapper<RolePermissionRelation>().lambda()
                .eq(RolePermissionRelation::getRoleId, roleId));
        List<RolePermissionRelation> rolePermissionRelations = Lists.newArrayList();
        if (CollUtil.isEmpty(permissionInfos)) {
            return;
        }
        for (PermissionInfo permissionInfo : permissionInfos) {
            RolePermissionRelation rolePermissionRelation = new RolePermissionRelation();
            rolePermissionRelation.setRoleId(roleId);
            rolePermissionRelation.setPermissionId(permissionInfo.getId());
            rolePermissionRelations.add(rolePermissionRelation);
        }
        super.saveBatch(rolePermissionRelations);
    }
}
