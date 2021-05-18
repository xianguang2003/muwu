/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.service.impl;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fanmu.muwu.service.provider.user.api.model.dto.permission.PermissionInfo;
import com.fanmu.muwu.service.provider.user.model.domain.Permission;
import com.fanmu.muwu.service.provider.user.model.domain.PermissionCategory;
import com.fanmu.muwu.service.provider.user.mapper.PermissionCategoryMapper;
import com.fanmu.muwu.service.provider.user.model.domain.PermissionPermissionCategoryRelation;
import com.fanmu.muwu.service.provider.user.service.PermissionCategoryService;
import com.fanmu.muwu.common.coer.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fanmu.muwu.common.base.enums.ErrorCodeEnum;
import com.fanmu.muwu.common.base.enums.GlobalStatusEnum;
import com.fanmu.muwu.common.base.pojo.BaseDTO;
import com.fanmu.muwu.common.coer.util.MyBatisUtil;
import com.fanmu.muwu.service.provider.user.api.exceptions.UserBizException;
import com.fanmu.muwu.service.provider.user.service.PermissionPermissionCategoryRelationService;
import com.fanmu.muwu.service.provider.user.service.PermissionService;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import com.fanmu.muwu.service.provider.user.api.service.PermissionCategoryRpcApi;
import com.fanmu.muwu.service.provider.user.api.model.dto.permissioncategory.PermissionCategoryInfo;
import com.fanmu.muwu.service.provider.user.api.model.query.permissioncategory.QueryPermissionCategory;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * <p>
 * 权限类别 服务实现类
 * </p>
 *
 * @author mumu
 * @since 2021-03-24
 */
@DubboService(interfaceClass = PermissionCategoryRpcApi.class)
public class PermissionCategoryServiceImpl extends ServiceImpl<PermissionCategoryMapper, PermissionCategory> implements PermissionCategoryService {

    @Autowired
    PermissionService permissionService;

    @Autowired
    PermissionPermissionCategoryRelationService permissionPermissionCategoryRelationService;

    @Override
    public void insertPermissionCategory(PermissionCategoryInfo permissionCategoryInfo) {
        PermissionCategory permissionCategory = super.getOne(new LambdaQueryWrapper<PermissionCategory>().eq(PermissionCategory::getCategoryName, permissionCategoryInfo.getCategoryName()));
        if (permissionCategory != null) {
            throw new UserBizException(ErrorCodeEnum.GL10000003, permissionCategory.getId());
        }
        permissionCategory = new PermissionCategory();
        BeanUtil.copyProperties(permissionCategoryInfo, permissionCategory);
        super.save(permissionCategory);
    }

    @Override
    public void deletePermissionCategoryById(Long id) {
        Preconditions.checkArgument(id != null, ErrorCodeEnum.GL10000001.msg());

        super.removeById(id);
    }

    @Override
    public void updatePermissionCategory(PermissionCategoryInfo permissionCategoryInfo) {
        Preconditions.checkArgument(permissionCategoryInfo.getId() != null, ErrorCodeEnum.GL10000001.msg());
        PermissionCategory permissionCategory = super.getById(permissionCategoryInfo.getId());
        if (permissionCategory == null) {
            throw new UserBizException(ErrorCodeEnum.GL10000002, permissionCategory.getId());
        }
        // 修改数据
        permissionCategory = new PermissionCategory();
        BeanUtil.copyProperties(permissionCategoryInfo, permissionCategory);
        super.updateById(permissionCategory);
    }

    @Override
    public PermissionCategoryInfo getPermissionCategoryById(Long id) {
        PermissionCategory permissionCategory = super.getById(id);
        if (permissionCategory == null) {
            throw new UserBizException(ErrorCodeEnum.GL10000002, id);
        }

        List<PermissionPermissionCategoryRelation> permissionPermissionCategoryRelations = permissionPermissionCategoryRelationService.list(new LambdaQueryWrapper<PermissionPermissionCategoryRelation>()
                .eq(PermissionPermissionCategoryRelation::getPermissionCategoryId, permissionCategory.getId()));
        List<PermissionInfo> permissionInfos = Lists.newArrayList();
        if (CollUtil.isNotEmpty(permissionPermissionCategoryRelations)) {
            List<Permission> permissions = permissionService.list(new LambdaQueryWrapper<Permission>()
                    .in(Permission::getId, permissionPermissionCategoryRelations.stream().map(PermissionPermissionCategoryRelation::getPermissionId).collect(Collectors.toList())));
            permissions.forEach(permission -> {
                PermissionInfo permissionInfo = new PermissionInfo();
                BeanUtil.copyProperties(permission, permissionInfo);
                permissionInfos.add(permissionInfo);
            });
        }

        PermissionCategoryInfo permissionCategoryInfo = new PermissionCategoryInfo();
        permissionCategoryInfo.setPermissionInfos(permissionInfos);
        BeanUtil.copyProperties(permissionCategory, permissionCategoryInfo);
        return permissionCategoryInfo;
    }

    @Override
    public List<PermissionCategoryInfo> listPermissionCategory() {
        return listPermissionCategory(null);
    }

    @Override
    public List<PermissionCategoryInfo> listPermissionCategory(QueryPermissionCategory queryPermissionCategory) {
        LambdaQueryWrapper<PermissionCategory> permissionCategoryQueryWrapper = new LambdaQueryWrapper<>();
        if (queryPermissionCategory != null) {
            if (StringUtils.isNotEmpty(queryPermissionCategory.getKeyword())) {
                permissionCategoryQueryWrapper.like(PermissionCategory::getCategoryName, queryPermissionCategory.getKeyword());
            }
        }
        List<PermissionCategory> permissionCategorys = super.list(permissionCategoryQueryWrapper);
        Map<Long, List<PermissionInfo>> permissionMap = Maps.newHashMap();
        if (CollUtil.isNotEmpty(permissionCategorys)) {
            List<PermissionPermissionCategoryRelation> permissionPermissionCategoryRelations = permissionPermissionCategoryRelationService.list(new LambdaQueryWrapper<PermissionPermissionCategoryRelation>()
                    .in(PermissionPermissionCategoryRelation::getPermissionCategoryId, permissionCategorys.stream().map(PermissionCategory::getId).collect(Collectors.toList())));
            if (CollUtil.isNotEmpty(permissionPermissionCategoryRelations)) {
                List<Permission> permissions = permissionService.list(new LambdaQueryWrapper<Permission>()
                        .in(Permission::getId, permissionPermissionCategoryRelations.stream().map(PermissionPermissionCategoryRelation::getPermissionId).collect(Collectors.toList())));
                Map<Long, List<Long>> collect = permissionPermissionCategoryRelations.stream()
                        .collect(Collectors.groupingBy(PermissionPermissionCategoryRelation::getPermissionCategoryId, Collectors.mapping(PermissionPermissionCategoryRelation::getPermissionId, Collectors.toList())));
                for (Map.Entry<Long, List<Long>> entry : collect.entrySet()) {
                    permissionMap.put(entry.getKey(), permissions.stream().filter(permission -> entry.getValue().contains(permission.getId())).map(permission -> {
                        PermissionInfo permissionInfo = new PermissionInfo();
                        BeanUtil.copyProperties(permission, permissionInfo);
                        return permissionInfo;
                    }).collect(Collectors.toList()));
                }
            }
        }
        List<PermissionCategoryInfo> permissionCategoryInfos = Lists.newArrayList();
        permissionCategorys.forEach(permissionCategory -> {
            PermissionCategoryInfo permissionCategoryInfo = new PermissionCategoryInfo();
            permissionCategoryInfo.setPermissionInfos(permissionMap.get(permissionCategory.getId()));
            BeanUtil.copyProperties(permissionCategory, permissionCategoryInfo);
            permissionCategoryInfos.add(permissionCategoryInfo);
        });
        return permissionCategoryInfos;
    }

    @Override
    public BaseDTO<PermissionCategoryInfo> listPermissionCategoryPage(QueryPermissionCategory queryPermissionCategory) {
        Page page = MyBatisUtil.page(queryPermissionCategory);
        LambdaQueryWrapper<PermissionCategory> permissionCategoryQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(queryPermissionCategory.getKeyword())) {
            permissionCategoryQueryWrapper.like(PermissionCategory::getCategoryName, queryPermissionCategory.getKeyword());
        }
        IPage<PermissionCategory> permissionCategoryIPage = super.page(page, permissionCategoryQueryWrapper);

        BaseDTO<PermissionCategoryInfo> baseDTO = MyBatisUtil.returnPage(permissionCategoryIPage);
        List<PermissionCategoryInfo> permissionCategoryInfos = Lists.newArrayList();
        baseDTO.setData(permissionCategoryInfos);
        permissionCategoryIPage.getRecords().forEach(permissionCategory -> {
            PermissionCategoryInfo permissionCategoryInfo = new PermissionCategoryInfo();
            BeanUtil.copyProperties(permissionCategory, permissionCategoryInfo);
            permissionCategoryInfos.add(permissionCategoryInfo);
        });
        return baseDTO;
    }

    @Override
    public void generatePermission(List<PermissionCategoryInfo> permissionCategoryInfos) {
        if (CollUtil.isEmpty(permissionCategoryInfos)) {
            return;
        }
        for (PermissionCategoryInfo permissionCategoryInfo : permissionCategoryInfos) {
            PermissionCategory permissionCategory = super.getOne(Wrappers.lambdaQuery(PermissionCategory.class).eq(PermissionCategory::getCategoryName, permissionCategoryInfo.getCategoryName()));
            if (permissionCategory == null) {
                permissionCategory = new PermissionCategory();
                BeanUtil.copyProperties(permissionCategoryInfo, permissionCategory);
                super.save(permissionCategory);
            }
            List<PermissionInfo> permissionInfos = permissionCategoryInfo.getPermissionInfos();
            for (PermissionInfo permissionInfo : permissionInfos) {
                Permission permission = permissionService.getOne(Wrappers.lambdaQuery(Permission.class).eq(Permission::getPermissionCode, permissionInfo.getPermissionCode()));
                if (permission == null) {
                    permission = new Permission();
                    BeanUtil.copyProperties(permissionInfo, permission);
                    permissionService.save(permission);
                    PermissionPermissionCategoryRelation permissionPermissionCategoryRelation = new PermissionPermissionCategoryRelation();
                    permissionPermissionCategoryRelation.setPermissionId(permission.getId());
                    permissionPermissionCategoryRelation.setPermissionCategoryId(permissionCategory.getId());
                    permissionPermissionCategoryRelationService.save(permissionPermissionCategoryRelation);
                }
            }
        }
    }
}
