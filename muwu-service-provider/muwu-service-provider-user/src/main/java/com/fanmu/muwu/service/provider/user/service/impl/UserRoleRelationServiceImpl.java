/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fanmu.muwu.service.provider.user.api.model.dto.role.RoleInfo;
import com.fanmu.muwu.service.provider.user.model.domain.UserRoleRelation;
import com.fanmu.muwu.service.provider.user.mapper.UserRoleRelationMapper;
import com.fanmu.muwu.service.provider.user.service.UserRoleRelationService;
import com.fanmu.muwu.common.coer.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户角色关联 服务实现类
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
@Service
public class UserRoleRelationServiceImpl extends ServiceImpl<UserRoleRelationMapper, UserRoleRelation> implements UserRoleRelationService {

    @Override
    public void addUserRole(List<RoleInfo> roleInfos, Long userId) {
        super.remove(new QueryWrapper<UserRoleRelation>().lambda()
                .eq(UserRoleRelation::getUserId, userId));
        List<UserRoleRelation> userRoleRelations = Lists.newArrayList();
        for (RoleInfo roleInfo : roleInfos) {
            UserRoleRelation userRoleRelation = new UserRoleRelation();
            userRoleRelation.setUserId(userId);
            userRoleRelation.setRoleId(roleInfo.getId());
            userRoleRelations.add(userRoleRelation);
        }
        super.saveBatch(userRoleRelations);
    }

}
