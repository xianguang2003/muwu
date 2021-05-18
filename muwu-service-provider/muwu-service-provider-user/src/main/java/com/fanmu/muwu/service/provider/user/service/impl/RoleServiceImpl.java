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
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanmu.muwu.common.base.constant.RedisConstant;
import com.fanmu.muwu.common.base.enums.ErrorCodeEnum;
import com.fanmu.muwu.common.base.enums.GlobalStatusEnum;
import com.fanmu.muwu.common.base.pojo.BaseDTO;
import com.fanmu.muwu.common.coer.extension.service.impl.ServiceImpl;
import com.fanmu.muwu.common.coer.util.MyBatisUtil;
import com.fanmu.muwu.service.provider.user.api.constant.MenuConstant;
import com.fanmu.muwu.service.provider.user.api.exceptions.UserBizException;
import com.fanmu.muwu.service.provider.user.api.model.dto.menu.MenuInfo;
import com.fanmu.muwu.service.provider.user.api.model.dto.permission.PermissionInfo;
import com.fanmu.muwu.service.provider.user.api.model.dto.role.RoleInfo;
import com.fanmu.muwu.service.provider.user.api.model.query.role.QueryRole;
import com.fanmu.muwu.service.provider.user.api.service.RoleRpcApi;
import com.fanmu.muwu.service.provider.user.mapper.RoleMapper;
import com.fanmu.muwu.service.provider.user.model.domain.*;
import com.fanmu.muwu.service.provider.user.model.expand.RolePermission;
import com.fanmu.muwu.service.provider.user.service.*;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色信息 服务实现类
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
@DubboService(interfaceClass = RoleRpcApi.class)
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    UserRoleRelationService userRoleRelationService;

    @Autowired
    UserGroupRoleRelationService userGroupRoleRelationService;

    @Autowired
    RoleMenuRelationService roleMenuRelationService;

    @Autowired
    RoleElementRelationService roleElementRelationService;

    @Autowired
    RolePermissionRelationService rolePermissionRelationService;

    @Autowired
    MenuService menuService;

    @Autowired
    PermissionService permissionService;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Override
    public void insertRole(RoleInfo roleInfo) {
        Role role = super.getOne(new LambdaQueryWrapper<Role>().eq(Role::getRoleCode, roleInfo.getRoleCode()));
        if (role != null) {
            throw new UserBizException(ErrorCodeEnum.GL10000003, role.getId());
        }
        role = new Role();
        BeanUtil.copyProperties(roleInfo, role);
        super.save(role);
        // 添加菜单
        roleMenuRelationService.addRoleMenu(roleInfo.getMenuInfos(), role.getId());
        // 更新缓存角色权限
        refreshCache(roleInfo.getRoleCode());
    }

    @Override
    public void deleteRoleById(Long id) {
        Preconditions.checkArgument(id != null, ErrorCodeEnum.USER10012001.msg());
        Role role = super.getById(id);
        if (role == null) {
            return;
        }

        super.removeById(id);
        userRoleRelationService.remove(new LambdaQueryWrapper<UserRoleRelation>().eq(UserRoleRelation::getRoleId, id));
        userGroupRoleRelationService.remove(new LambdaQueryWrapper<UserGroupRoleRelation>().eq(UserGroupRoleRelation::getRoleId, id));
        roleMenuRelationService.remove(new LambdaQueryWrapper<RoleMenuRelation>().eq(RoleMenuRelation::getRoleId, id));
        roleElementRelationService.remove(new LambdaQueryWrapper<RoleElementRelation>().eq(RoleElementRelation::getRoleId, id));
        rolePermissionRelationService.remove(new LambdaQueryWrapper<RolePermissionRelation>().eq(RolePermissionRelation::getRoleId, id));
        // 更新缓存角色权限
        refreshCache(role.getRoleCode());
    }

    @Override
    public void updateRole(RoleInfo roleInfo) {
        Preconditions.checkArgument(roleInfo.getId() != null, ErrorCodeEnum.GL10000001.msg());
        Role role = super.getById(roleInfo.getId());
        if (role == null) {
            throw new UserBizException(ErrorCodeEnum.GL10000002, role.getId());
        }
        // 添加菜单
        roleMenuRelationService.addRoleMenu(roleInfo.getMenuInfos(), role.getId());
        // 修改数据
        role = new Role();
        BeanUtil.copyProperties(roleInfo, role);
        super.updateById(role);
        // 更新缓存角色权限
        refreshCache(roleInfo.getRoleCode());
    }

    @Override
    public RoleInfo getRoleById(Long id) {
        Role role = super.getById(id);
        if (role == null) {
            throw new UserBizException(ErrorCodeEnum.USER10012002, id);
        }
        List<RoleMenuRelation> roleMenuRelations = roleMenuRelationService.list(new LambdaQueryWrapper<RoleMenuRelation>()
                .eq(RoleMenuRelation::getRoleId, role.getId()));
        List<MenuInfo> menuInfos = Lists.newArrayList();
        if (CollUtil.isNotEmpty(roleMenuRelations)) {
            List<Menu> menus = menuService.list(new LambdaQueryWrapper<Menu>()
                    .in(Menu::getId, roleMenuRelations.stream().map(RoleMenuRelation::getMenuId).collect(Collectors.toList()))
                    .eq(Menu::getStatus, GlobalStatusEnum.ENABLE.getStatus())
                    .eq(Menu::getLeaf, MenuConstant.MENU_LEAF_YES));
            menus.forEach(menu -> {
                MenuInfo menuInfo = new MenuInfo();
                BeanUtil.copyProperties(menu, menuInfo);
                menuInfos.add(menuInfo);
            });
        }
        List<RolePermissionRelation> rolePermissionRelations = rolePermissionRelationService.list(new LambdaQueryWrapper<RolePermissionRelation>()
                .eq(RolePermissionRelation::getRoleId, role.getId()));
        List<PermissionInfo> permissionInfos = Lists.newArrayList();
        if (CollUtil.isNotEmpty(rolePermissionRelations)) {
            List<Permission> permissions = permissionService.list(new LambdaQueryWrapper<Permission>()
                    .in(Permission::getId, rolePermissionRelations.stream().map(RolePermissionRelation::getPermissionId).collect(Collectors.toList()))
                    .eq(Permission::getStatus, GlobalStatusEnum.ENABLE.getStatus()));
            permissions.forEach(permission -> {
                PermissionInfo permissionInfo = new PermissionInfo();
                BeanUtil.copyProperties(permission, permissionInfo);
                permissionInfos.add(permissionInfo);
            });
        }

        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setMenuInfos(menuInfos);
        roleInfo.setPermissionInfos(permissionInfos);
        BeanUtil.copyProperties(role, roleInfo);
        return roleInfo;
    }

    @Override
    public List<RoleInfo> listRole() {
        return listRole(null);
    }

    @Override
    public List<RoleInfo> listRole(QueryRole queryRole) {
        LambdaQueryWrapper<Role> roleQueryWrapper = new LambdaQueryWrapper<>();
        roleQueryWrapper.eq(Role::getStatus, GlobalStatusEnum.ENABLE.getStatus());
        if (queryRole != null) {
            if (StringUtils.isNotEmpty(queryRole.getKeyword())) {
                roleQueryWrapper.like(Role::getRoleName, queryRole.getKeyword())
                        .or().like(Role::getRoleCode, queryRole.getKeyword());
            }
        }
        List<Role> roles = super.list(roleQueryWrapper);
        List<RoleInfo> roleInfos = Lists.newArrayList();
        roles.forEach(role -> {
            RoleInfo roleInfo = new RoleInfo();
            BeanUtil.copyProperties(role, roleInfo);
            roleInfos.add(roleInfo);
        });
        return roleInfos;
    }

    @Override
    public BaseDTO<RoleInfo> listRolePage(QueryRole queryRole) {
        Page page = MyBatisUtil.page(queryRole);
        LambdaQueryWrapper<Role> roleQueryWrapper = new LambdaQueryWrapper<>();
        roleQueryWrapper.eq(Role::getStatus, GlobalStatusEnum.ENABLE.getStatus());
        if (StringUtils.isNotEmpty(queryRole.getKeyword())) {
            roleQueryWrapper.like(Role::getRoleName, queryRole.getKeyword())
                    .or().like(Role::getRoleCode, queryRole.getKeyword());
        }
        IPage<Role> roleIPage = super.page(page, roleQueryWrapper);

        BaseDTO<RoleInfo> baseDTO = MyBatisUtil.returnPage(roleIPage);
        List<RoleInfo> roleInfos = Lists.newArrayList();
        baseDTO.setData(roleInfos);
        roleIPage.getRecords().forEach(role -> {
            RoleInfo roleInfo = new RoleInfo();
            BeanUtil.copyProperties(role, roleInfo);
            roleInfos.add(roleInfo);
        });
        return baseDTO;
    }

    @Override
    public void saveRoleMenu(RoleInfo roleInfo) {
        List<MenuInfo> menuInfos = roleInfo.getMenuInfos();
        if (CollUtil.isEmpty(menuInfos)) {
            throw new UserBizException(ErrorCodeEnum.USER10014005);
        }
        List<Menu> menus = menuService.list(new LambdaQueryWrapper<Menu>()
                .eq(Menu::getStatus, GlobalStatusEnum.ENABLE.getStatus())
                .in(Menu::getId, menuInfos.stream().map(MenuInfo::getId).collect(Collectors.toList())));
        if (CollUtil.isEmpty(menus)) {
            throw new UserBizException(ErrorCodeEnum.USER10014005);
        }
        // 删除菜单ID
        roleMenuRelationService.remove(new LambdaQueryWrapper<RoleMenuRelation>().eq(RoleMenuRelation::getRoleId, roleInfo.getId()));
        // 新增菜单ID
        List<RoleMenuRelation> roleMenuRelations = Lists.newArrayList();
        for (MenuInfo menuInfo : menuInfos) {
            RoleMenuRelation roleMenuRelation = new RoleMenuRelation();
            roleMenuRelation.setRoleId(roleInfo.getId());
            roleMenuRelation.setMenuId(menuInfo.getId());
            roleMenuRelations.add(roleMenuRelation);
        }
        roleMenuRelationService.saveBatch(roleMenuRelations);
        // 更新缓存角色权限
        refreshCache(roleInfo.getRoleCode());
    }

    @Override
    public void refreshCache(String roleCode) {
        List<RolePermission> rolePermissions = baseMapper.listRolePermissionUri(roleCode);
        if (CollUtil.isEmpty(rolePermissions)) {
            if (StringUtils.isNotEmpty(roleCode)) {
                redisTemplate.opsForHash().delete(RedisConstant.ROLE_PERMISSION_URL_KEY, roleCode);
            }
            return;
        }
        for (RolePermission rolePermission : rolePermissions) {
            List<String> urls = Lists.newArrayList();
            CollUtil.addAll(urls, rolePermission.getMenuUrl());
            CollUtil.addAll(urls, rolePermission.getElementUrl());
            CollUtil.addAll(urls, rolePermission.getPermissionUrl());
            CollUtil.addAll(urls, rolePermission.getMenuPermissionUrl());
            CollUtil.addAll(urls, rolePermission.getElementPermissionUrl());
            redisTemplate.opsForHash().put(RedisConstant.ROLE_PERMISSION_URL_KEY, rolePermission.getRoleCode(), JSONObject.toJSONString(urls.toArray(new String[0])));
        }
    }

    @Override
    public void refreshCache() {
        List<Role> roles = super.list();
        roles.forEach(role -> {
            refreshCache(role.getRoleCode());
        });
    }

    @Override
    public void updateRolePermission(RoleInfo roleInfo) {
        Preconditions.checkArgument(roleInfo.getId() != null, ErrorCodeEnum.GL10000001.msg());
        Role role = super.getById(roleInfo.getId());
        if (role == null) {
            throw new UserBizException(ErrorCodeEnum.GL10000002, role.getId());
        }
        // 添加权限
        rolePermissionRelationService.addRolePermission(roleInfo.getPermissionInfos(), role.getId());
        // 更新缓存角色权限
        refreshCache(roleInfo.getRoleCode());
    }
}
