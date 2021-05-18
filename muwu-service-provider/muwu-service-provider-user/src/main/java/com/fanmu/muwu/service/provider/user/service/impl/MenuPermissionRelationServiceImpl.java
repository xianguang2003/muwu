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
import com.fanmu.muwu.common.coer.extension.service.impl.ServiceImpl;
import com.fanmu.muwu.service.provider.user.api.model.dto.permission.PermissionInfo;
import com.fanmu.muwu.service.provider.user.mapper.MenuPermissionRelationMapper;
import com.fanmu.muwu.service.provider.user.model.domain.MenuPermissionRelation;
import com.fanmu.muwu.service.provider.user.service.MenuPermissionRelationService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <p>
 * 菜单权限关联 服务实现类
 * </p>
 *
 * @author mumu
 * @since 2021-03-29
 */
@Service
public class MenuPermissionRelationServiceImpl extends ServiceImpl<MenuPermissionRelationMapper, MenuPermissionRelation> implements MenuPermissionRelationService {

    @Override
    public void addMenuPermission(List<PermissionInfo> permissionInfos, Long menuId) {
        super.remove(new QueryWrapper<MenuPermissionRelation>().lambda()
                .eq(MenuPermissionRelation::getMenuId, menuId));
        List<MenuPermissionRelation> menuPermissionRelations = Lists.newArrayList();
        if (CollUtil.isEmpty(permissionInfos)) {
            return;
        }
        for (PermissionInfo permissionInfo : permissionInfos) {
            MenuPermissionRelation menuPermissionRelation = new MenuPermissionRelation();
            menuPermissionRelation.setMenuId(menuId);
            menuPermissionRelation.setPermissionId(permissionInfo.getId());
            menuPermissionRelations.add(menuPermissionRelation);
        }
        super.saveBatch(menuPermissionRelations);
    }

}
