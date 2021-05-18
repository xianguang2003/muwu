/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanmu.muwu.common.base.constant.RedisConstant;
import com.fanmu.muwu.common.base.enums.ErrorCodeEnum;
import com.fanmu.muwu.common.base.enums.GlobalStatusEnum;
import com.fanmu.muwu.common.base.pojo.BaseDTO;
import com.fanmu.muwu.common.coer.extension.service.impl.ServiceImpl;
import com.fanmu.muwu.common.coer.util.MyBatisUtil;
import com.fanmu.muwu.service.provider.user.api.enums.PermissionRoleShareEnum;
import com.fanmu.muwu.service.provider.user.api.enums.PermissionWhitelistEnum;
import com.fanmu.muwu.service.provider.user.api.exceptions.UserBizException;
import com.fanmu.muwu.service.provider.user.api.model.dto.permission.PermissionInfo;
import com.fanmu.muwu.service.provider.user.api.model.dto.permissioncategory.PermissionCategoryInfo;
import com.fanmu.muwu.service.provider.user.api.model.dto.role.RoleInfo;
import com.fanmu.muwu.service.provider.user.api.model.query.permission.QueryPermission;
import com.fanmu.muwu.service.provider.user.api.service.PermissionRpcApi;
import com.fanmu.muwu.service.provider.user.mapper.PermissionMapper;
import com.fanmu.muwu.service.provider.user.model.domain.*;
import com.fanmu.muwu.service.provider.user.service.*;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限 服务实现类
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
@DubboService(interfaceClass = PermissionRpcApi.class)
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    PermissionCategoryService permissionCategoryService;

    @Autowired
    PermissionPermissionCategoryRelationService permissionPermissionCategoryRelationService;

    @Autowired
    RoleService roleService;

    @Autowired
    RolePermissionRelationService rolePermissionRelationService;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Override
    public void insertPermission(PermissionInfo permissionInfo) {
        Permission permission = super.getOne(Wrappers.lambdaQuery(Permission.class).eq(Permission::getPermissionCode, permissionInfo.getPermissionCode()));
        if (permission != null) {
            throw new UserBizException(ErrorCodeEnum.USER10013001, permission.getId());
        }
        permission = new Permission();
        BeanUtil.copyProperties(permissionInfo, permission);
        super.save(permission);
        // 分类设置
        permissionPermissionCategoryRelationService.addPermissionPermissionCategory(permissionInfo.getPermissionCategoryInfos(), permission.getId());
        // 刷新白名单
        refreshCache(permission);
    }

    @Override
    public void deletePermissionById(Long id) {
        Preconditions.checkArgument(id != null, ErrorCodeEnum.USER10013002.msg());

        Permission permission = super.getById(id);
        if (permission == null) {
            return;
        }

        super.removeById(id);
        // 刷新白名单
        permission.setWhitelist(PermissionWhitelistEnum.NO.getValue());
        refreshCache(permission);
    }

    @Override
    public void updatePermission(PermissionInfo permissionInfo) {
        Preconditions.checkArgument(permissionInfo.getId() != null, ErrorCodeEnum.USER10013003.msg());
        Permission permission = super.getById(permissionInfo.getId());
        if (permission == null) {
            throw new UserBizException(ErrorCodeEnum.GL10000002, permission.getId());
        }
        // 修改数据
        permission = new Permission();
        BeanUtil.copyProperties(permissionInfo, permission);
        super.updateById(permission);
        // 分类设置
        permissionPermissionCategoryRelationService.addPermissionPermissionCategory(permissionInfo.getPermissionCategoryInfos(), permission.getId());
        // 刷新白名单
        refreshCache(permission);
        refreshPermissionRoleCache(permission.getId());
    }

    @Override
    public PermissionInfo getPermissionById(Long id) {
        Permission permission = super.getById(id);
        if (permission == null) {
            throw new UserBizException(ErrorCodeEnum.GL10000002, id);
        }

        List<PermissionPermissionCategoryRelation> permissionPermissionCategoryRelations = permissionPermissionCategoryRelationService.list(Wrappers.lambdaQuery(PermissionPermissionCategoryRelation.class)
                .eq(PermissionPermissionCategoryRelation::getPermissionId, permission.getId()));
        List<PermissionCategoryInfo> permissionCategoryInfos = Lists.newArrayList();
        if (CollUtil.isNotEmpty(permissionPermissionCategoryRelations)) {
            List<PermissionCategory> permissionCategories = permissionCategoryService.list(Wrappers.lambdaQuery(PermissionCategory.class)
                    .in(PermissionCategory::getId, permissionPermissionCategoryRelations.stream().map(PermissionPermissionCategoryRelation::getPermissionCategoryId).collect(Collectors.toList())));
            permissionCategories.forEach(permissionCategory -> {
                PermissionCategoryInfo permissionCategoryInfo = new PermissionCategoryInfo();
                BeanUtil.copyProperties(permissionCategory, permissionCategoryInfo);
                permissionCategoryInfos.add(permissionCategoryInfo);
            });
        }
        PermissionInfo permissionInfo = new PermissionInfo();
        permissionInfo.setPermissionCategoryInfos(permissionCategoryInfos);
        BeanUtil.copyProperties(permission, permissionInfo);
        return permissionInfo;
    }

    @Override
    public List<PermissionInfo> listPermission() {
        return listPermission(null);
    }

    @Override
    public List<PermissionInfo> listPermission(QueryPermission queryPermission) {
        LambdaQueryWrapper<Permission> permissionQueryWrapper = Wrappers.lambdaQuery();
        permissionQueryWrapper.eq(Permission::getStatus, GlobalStatusEnum.ENABLE.getStatus());
        if (queryPermission != null) {
            if (StringUtils.isNotEmpty(queryPermission.getKeyword())) {
                permissionQueryWrapper.like(Permission::getPermissionName, queryPermission.getKeyword())
                        .or().like(Permission::getPermissionCode, queryPermission.getKeyword());
            }
            if (queryPermission.getWhitelist() != null) {
                permissionQueryWrapper.eq(Permission::getWhitelist, queryPermission.getWhitelist());
            }
            if (queryPermission.getRoleSharePermission() != null) {
                permissionQueryWrapper.eq(Permission::getRoleSharePermission, queryPermission.getRoleSharePermission());
            }
        }
        List<Permission> permissions = super.list(permissionQueryWrapper);
        List<PermissionInfo> permissionInfos = Lists.newArrayList();
        permissions.forEach(permission -> {
            PermissionInfo permissionInfo = new PermissionInfo();
            BeanUtil.copyProperties(permission, permissionInfo);
            permissionInfos.add(permissionInfo);
        });
        return permissionInfos;
    }

    @Override
    public BaseDTO<PermissionInfo> listPermissionPage(QueryPermission queryPermission) {
        Page page = MyBatisUtil.page(queryPermission);
        LambdaQueryWrapper<Permission> permissionQueryWrapper = Wrappers.lambdaQuery();
        permissionQueryWrapper.eq(Permission::getStatus, GlobalStatusEnum.ENABLE.getStatus());
        if (StringUtils.isNotEmpty(queryPermission.getKeyword())) {
            permissionQueryWrapper.like(Permission::getPermissionName, queryPermission.getKeyword())
                    .or().like(Permission::getPermissionCode, queryPermission.getKeyword());
        }
        IPage<Permission> permissionIPage = super.page(page, permissionQueryWrapper);

        BaseDTO<PermissionInfo> baseDTO = MyBatisUtil.returnPage(permissionIPage);
        List<PermissionInfo> permissionInfos = Lists.newArrayList();
        baseDTO.setData(permissionInfos);
        permissionIPage.getRecords().forEach(permission -> {
            PermissionInfo permissionInfo = new PermissionInfo();
            BeanUtil.copyProperties(permission, permissionInfo);
            permissionInfos.add(permissionInfo);
        });
        return baseDTO;
    }

    @Override
    public void refreshCache(Permission permission) {
        if (permission == null) {
            return;
        }
        if (permission.getWhitelist() == PermissionWhitelistEnum.YES.getValue()) {
            redisTemplate.opsForSet().add(RedisConstant.PERMISSION_WHITELIST_URLS_KEY, permission.getUrl());
        } else {
            redisTemplate.opsForSet().remove(RedisConstant.PERMISSION_WHITELIST_URLS_KEY, permission.getUrl());
        }
    }

    @Override
    public void refreshCache() {
        List<Permission> permissions = super.list();
        permissions.forEach(permission -> {
            refreshCache(permission);
        });
    }

    @Override
    public void batchUpdateWhitelist(List<PermissionInfo> permissionInfos) {
        List<Permission> permissions = Lists.newArrayList();
        for (PermissionInfo permissionInfo : permissionInfos) {
            if (permissionInfo.getId() == null) {
                continue;
            }
            Permission permission = new Permission();
            permission.setId(permissionInfo.getId());
            permission.setWhitelist(permissionInfo.getWhitelist());
            permissions.add(permission);
        }
        lambdaUpdate().set(Permission::getWhitelist, PermissionWhitelistEnum.NO.getValue()).update();
        super.updateBatchById(permissions);
        this.refreshCache();
    }

    @Override
    public void batchUpdateRoleSharePermission(List<PermissionInfo> permissionInfos) {
        List<Permission> sharePermissions = Lists.newArrayList();
        for (PermissionInfo permissionInfo : permissionInfos) {
            if (permissionInfo.getId() == null) {
                continue;
            }
            Permission permission = new Permission();
            permission.setId(permissionInfo.getId());
            permission.setRoleSharePermission(permissionInfo.getRoleSharePermission());
            sharePermissions.add(permission);
        }
        List<Long> removePermissionIds = Lists.newArrayList();
        List<Long> addPermissionIds = Lists.newArrayList();
        List<Permission> permissions = super.list(Wrappers.lambdaQuery(Permission.class).eq(Permission::getRoleSharePermission, PermissionRoleShareEnum.YES.getValue()));
        Map<Long, Permission> sharePermissionMap = sharePermissions.stream().collect(Collectors.toMap(Permission::getId, permission -> permission));
        Map<Long, Permission> permissionMap = permissions.stream().collect(Collectors.toMap(Permission::getId, permission -> permission));
        for (Permission permission : permissions) {
            Permission sharePermission = sharePermissionMap.get(permission.getId());
            if (sharePermission == null) {
                removePermissionIds.add(permission.getId());
            }
        }
        for (Permission permission : sharePermissions) {
            Permission sharePermission = permissionMap.get(permission.getId());
            if (sharePermission == null) {
                addPermissionIds.add(permission.getId());
            }
        }
        if (CollUtil.isEmpty(removePermissionIds) && CollUtil.isEmpty(addPermissionIds)) {
            return;
        }
        List<Role> roles = roleService.list();
        for (Role role : roles) {
            RoleInfo roleInfo = roleService.getRoleById(role.getId());
            List<PermissionInfo> roleInfoPermissionInfos = roleInfo.getPermissionInfos();
            Map<Long, PermissionInfo> permissionInfoMap = roleInfoPermissionInfos.stream().collect(Collectors.toMap(PermissionInfo::getId, permissionInfo -> permissionInfo));
            for (Long removePermissionId : removePermissionIds) {
                if (permissionInfoMap.get(removePermissionId) != null) {
                    roleInfoPermissionInfos.remove(permissionInfoMap.get(removePermissionId));
                }
            }
            for (Long addPermissionId : addPermissionIds) {
                if (permissionInfoMap.get(addPermissionId) == null) {
                    PermissionInfo permissionInfo = new PermissionInfo();
                    permissionInfo.setId(addPermissionId);
                    roleInfoPermissionInfos.add(permissionInfo);
                }
            }
            roleService.updateRolePermission(roleInfo);
        }
        lambdaUpdate().set(Permission::getRoleSharePermission, PermissionRoleShareEnum.NO.getValue()).update();
        super.updateBatchById(sharePermissions);
    }

    public void refreshPermissionRoleCache(Long permissionId) {
        List<RolePermissionRelation> rolePermissionRelations = rolePermissionRelationService.list(Wrappers.lambdaQuery(RolePermissionRelation.class).eq(RolePermissionRelation::getPermissionId, permissionId));
        if (CollUtil.isNotEmpty(rolePermissionRelations)) {
            List<Role> roles = roleService.list(Wrappers.lambdaQuery(Role.class).in(Role::getId, rolePermissionRelations.stream().map(RolePermissionRelation::getRoleId).collect(Collectors.toList())));
            roles.forEach(role -> {
                roleService.refreshCache(role.getRoleCode());
            });
        }
    }
}
