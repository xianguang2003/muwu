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
import com.fanmu.muwu.service.provider.user.api.model.dto.permissioncategory.PermissionCategoryInfo;
import com.fanmu.muwu.service.provider.user.mapper.PermissionPermissionCategoryRelationMapper;
import com.fanmu.muwu.service.provider.user.model.domain.PermissionPermissionCategoryRelation;
import com.fanmu.muwu.service.provider.user.service.PermissionPermissionCategoryRelationService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <p>
 * 权限类别权限关联 服务实现类
 * </p>
 *
 * @author mumu
 * @since 2021-03-24
 */
@Service
public class PermissionPermissionCategoryRelationServiceImpl extends ServiceImpl<PermissionPermissionCategoryRelationMapper, PermissionPermissionCategoryRelation> implements PermissionPermissionCategoryRelationService {

    @Override
    public void addPermissionPermissionCategory(List<PermissionCategoryInfo> permissionCategoryInfos, Long permissionId) {
        super.remove(new QueryWrapper<PermissionPermissionCategoryRelation>().lambda()
                .eq(PermissionPermissionCategoryRelation::getPermissionId, permissionId));
        List<PermissionPermissionCategoryRelation> permissionPermissionCategoryRelations = Lists.newArrayList();
        if (CollUtil.isEmpty(permissionCategoryInfos)) {
            return;
        }
        for (PermissionCategoryInfo permissionCategoryInfo : permissionCategoryInfos) {
            PermissionPermissionCategoryRelation permissionPermissionCategoryRelation = new PermissionPermissionCategoryRelation();
            permissionPermissionCategoryRelation.setPermissionId(permissionId);
            permissionPermissionCategoryRelation.setPermissionCategoryId(permissionCategoryInfo.getId());
            permissionPermissionCategoryRelations.add(permissionPermissionCategoryRelation);
        }
        super.saveBatch(permissionPermissionCategoryRelations);
    }

}
