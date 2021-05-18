/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fanmu.muwu.service.provider.user.api.model.dto.menu.MenuInfo;
import com.fanmu.muwu.service.provider.user.model.domain.RoleMenuRelation;
import com.fanmu.muwu.service.provider.user.mapper.RoleMenuRelationMapper;
import com.fanmu.muwu.service.provider.user.service.RoleMenuRelationService;
import com.fanmu.muwu.common.coer.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色菜单关联 服务实现类
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
@Service
public class RoleMenuRelationServiceImpl extends ServiceImpl<RoleMenuRelationMapper, RoleMenuRelation> implements RoleMenuRelationService {

    @Override
    public void addRoleMenu(List<MenuInfo> menuInfos, Long roleId) {
        super.remove(new QueryWrapper<RoleMenuRelation>().lambda()
                .eq(RoleMenuRelation::getRoleId, roleId));
        List<RoleMenuRelation> roleMenuRelations = Lists.newArrayList();
        for (MenuInfo menuInfo : menuInfos) {
            RoleMenuRelation roleMenuRelation = new RoleMenuRelation();
            roleMenuRelation.setRoleId(roleId);
            roleMenuRelation.setMenuId(menuInfo.getId());
            roleMenuRelations.add(roleMenuRelation);
        }
        super.saveBatch(roleMenuRelations);
    }

}
