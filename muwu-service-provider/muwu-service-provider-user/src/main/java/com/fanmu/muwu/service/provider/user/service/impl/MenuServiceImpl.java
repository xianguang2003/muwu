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
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanmu.muwu.common.base.enums.ErrorCodeEnum;
import com.fanmu.muwu.common.base.enums.GlobalStatusEnum;
import com.fanmu.muwu.common.base.pojo.BaseDTO;
import com.fanmu.muwu.common.coer.extension.service.impl.ServiceImpl;
import com.fanmu.muwu.common.coer.util.MyBatisUtil;
import com.fanmu.muwu.service.provider.user.api.constant.MenuConstant;
import com.fanmu.muwu.service.provider.user.api.exceptions.UserBizException;
import com.fanmu.muwu.service.provider.user.api.model.dto.menu.MenuInfo;
import com.fanmu.muwu.service.provider.user.api.model.dto.permission.PermissionInfo;
import com.fanmu.muwu.service.provider.user.api.model.query.menu.QueryMenu;
import com.fanmu.muwu.service.provider.user.api.service.MenuRpcApi;
import com.fanmu.muwu.service.provider.user.mapper.MenuMapper;
import com.fanmu.muwu.service.provider.user.model.domain.*;
import com.fanmu.muwu.service.provider.user.service.*;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单 服务实现类
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
@DubboService(interfaceClass = MenuRpcApi.class)
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    RoleMenuRelationService roleMenuRelationService;

    @Autowired
    MenuPermissionRelationService menuPermissionRelationService;

    @Autowired
    PermissionService permissionService;

    @Autowired
    RoleService roleService;

    public void insertMenu(MenuInfo menuInfo) {
        Menu parentMenu = super.getOne(new LambdaQueryWrapper<Menu>().eq(Menu::getId, menuInfo.getParentId()), false);
        if (parentMenu == null) {
            throw new UserBizException(ErrorCodeEnum.USER10014001, menuInfo.getParentId());
        }
        if (parentMenu.getLevel() == MenuConstant.MENU_LEVEL_THREE) {
            throw new UserBizException(ErrorCodeEnum.USER10014002, menuInfo.getParentId());
        }
        if (parentMenu.getLeaf() == MenuConstant.MENU_LEAF_YES) {
            Menu tmpMenu = new Menu();
            tmpMenu.setId(parentMenu.getId());
            tmpMenu.setLeaf(MenuConstant.MENU_LEAF_NO);
            super.updateById(tmpMenu);
        }
        int count = super.count(new LambdaQueryWrapper<Menu>().eq(Menu::getParentId, parentMenu.getId()));
        Menu menu = new Menu();
        BeanUtil.copyProperties(menuInfo, menu);
        menu.setLevel(parentMenu.getLevel() + 1);
        menu.setStatus(GlobalStatusEnum.ENABLE.getStatus());
        menu.setLeaf(MenuConstant.MENU_LEAF_YES);
        menu.setNumber(count);
        super.save(menu);
        // 权限设置
//        menuPermissionRelationService.addMenuPermission(menuInfo.getPermissionInfos(), menu.getId());
    }

    public void deleteMenuById(Long id) {
        Menu menu = super.getById(id);
        if (menu == null) {
            throw new UserBizException(ErrorCodeEnum.USER10014003, id);
        }
        // 判断是否为叶子节点
        if (!menu.getLeaf()) {
            throw new UserBizException(ErrorCodeEnum.USER10014004, id);
        }

        super.removeById(menu.getId());
        roleMenuRelationService.remove(new LambdaQueryWrapper<RoleMenuRelation>().eq(RoleMenuRelation::getMenuId, menu.getId()));
        menuPermissionRelationService.remove(new LambdaQueryWrapper<MenuPermissionRelation>().eq(MenuPermissionRelation::getMenuId, menu.getId()));

        // 更新老父节点
        int count = super.count(new LambdaQueryWrapper<Menu>().eq(Menu::getParentId, menu.getParentId()));
        if (count == 0) {
            Menu parentMenu = new Menu();
            parentMenu.setId(menu.getParentId());
            parentMenu.setLeaf(MenuConstant.MENU_LEAF_YES);
            super.updateById(parentMenu);
        } else {
            List<Menu> menus = super.list(new LambdaQueryWrapper<Menu>().eq(Menu::getParentId, menu.getParentId()).orderByAsc(Menu::getNumber));
            List<MenuInfo> menuInfos = Lists.newArrayList();
            int number = 0;
            for (Menu menuTmp : menus) {
                MenuInfo menuInfoTmp = new MenuInfo();
                menuInfoTmp.setId(menuTmp.getId());
                menuInfoTmp.setNumber(number++);
                menuInfos.add(menuInfoTmp);
            }
            this.sortMenu(menuInfos);
        }
    }

    public void updateMenu(MenuInfo menuInfo) {
        Menu menu = super.getById(menuInfo.getId());
        if (menu == null) {
            throw new UserBizException(ErrorCodeEnum.USER10014003, menuInfo.getId());
        }
        if (StringUtils.isNotEmpty(menuInfo.getMenuCode())) {
            Menu menuTmp = super.getOne(new LambdaQueryWrapper<Menu>().ne(Menu::getId, menu.getId())
                    .eq(Menu::getMenuCode, menuInfo.getMenuCode()));
            if (menuTmp != null) {
                throw new UserBizException(ErrorCodeEnum.USER10014003, menuInfo.getId());
            }
        }
        // 修改父节点
        if (menu.getParentId() != menuInfo.getParentId()) {
            // 更新父节点
            Menu parentMenu = super.getOne(new LambdaQueryWrapper<Menu>().eq(Menu::getId, menuInfo.getParentId()), false);
            if (parentMenu == null) {
                throw new UserBizException(ErrorCodeEnum.USER10014001, menuInfo.getParentId());
            }
            if (parentMenu.getLevel() == MenuConstant.MENU_LEVEL_THREE) {
                throw new UserBizException(ErrorCodeEnum.USER10014002, menuInfo.getParentId());
            }
            if (parentMenu.getLeaf() == MenuConstant.MENU_LEAF_YES) {
                Menu tmpMenu = new Menu();
                tmpMenu.setId(parentMenu.getId());
                tmpMenu.setLeaf(MenuConstant.MENU_LEAF_NO);
                super.updateById(tmpMenu);
            }
            // 更新老父节点的子节点排序
            List<Menu> menus = super.list(new LambdaQueryWrapper<Menu>().eq(Menu::getParentId, menu.getParentId()).orderByAsc(Menu::getNumber));
            List<MenuInfo> menuInfos = Lists.newArrayList();
            int number = 0;
            for (Menu menuTmp : menus) {
                MenuInfo menuInfoTmp = new MenuInfo();
                menuInfoTmp.setId(menuTmp.getId());
                if (menu.getId() != menuTmp.getId()) {
                    menuInfoTmp.setNumber(number++);
                    menuInfos.add(menuInfoTmp);
                }
            }
            this.sortMenu(menuInfos);
            // 更新新节点
            int count = super.count(new LambdaQueryWrapper<Menu>().eq(Menu::getParentId, parentMenu.getId()));
            menuInfo.setNumber(count);
            menuInfo.setLeaf(MenuConstant.MENU_LEAF_YES);
            menuInfo.setLevel(parentMenu.getLevel() + 1);
        }
        // 权限设置
//        menuPermissionRelationService.addMenuPermission(menuInfo.getPermissionInfos(), menu.getId());
        // 修改数据
        menu = new Menu();
        BeanUtil.copyProperties(menuInfo, menu);
        super.updateById(menu);
    }

    @Override
    public MenuInfo getMenuById(Long id) {
        Menu menu = super.getById(id);
        if (menu == null) {
            throw new UserBizException(ErrorCodeEnum.USER10014003, id);
        }
        List<MenuPermissionRelation> menuPermissionRelations = menuPermissionRelationService.list(new LambdaQueryWrapper<MenuPermissionRelation>()
                .eq(MenuPermissionRelation::getMenuId, menu.getId()));
        List<PermissionInfo> permissionInfos = Lists.newArrayList();
        if (CollUtil.isNotEmpty(menuPermissionRelations)) {
            List<Permission> permissions = permissionService.list(new LambdaQueryWrapper<Permission>()
                    .in(Permission::getId, menuPermissionRelations.stream().map(MenuPermissionRelation::getPermissionId).collect(Collectors.toList())));
            permissions.forEach(permission -> {
                PermissionInfo permissionInfo = new PermissionInfo();
                BeanUtil.copyProperties(permission, permissionInfo);
                permissionInfos.add(permissionInfo);
            });
        }
        MenuInfo menuInfo = new MenuInfo();
        menuInfo.setPermissionInfos(permissionInfos);
        BeanUtil.copyProperties(menu, menuInfo);
        return menuInfo;
    }

    @Override
    public List<MenuInfo> listMenu() {
        return listMenu(null);
    }

    @Override
    public List<MenuInfo> listMenu(QueryMenu queryMenu) {
        LambdaQueryWrapper<Menu> menuQueryWrapper = new LambdaQueryWrapper<>();
        menuQueryWrapper.eq(Menu::getStatus, GlobalStatusEnum.ENABLE.getStatus());
        if (queryMenu != null) {
            if (StringUtils.isNotEmpty(queryMenu.getKeyword())) {
                menuQueryWrapper.like(Menu::getMenuName, queryMenu.getKeyword())
                        .or().like(Menu::getMenuCode, queryMenu.getKeyword());
            }
        }
        List<Menu> menus = super.list(menuQueryWrapper);
        List<MenuInfo> menuInfos = Lists.newArrayList();
        menus.forEach(ucMenu -> {
            MenuInfo menuInfo = new MenuInfo();
            BeanUtil.copyProperties(ucMenu, menuInfo);
            menuInfos.add(menuInfo);
        });
        return menuInfos;
    }

    @Override
    public BaseDTO<MenuInfo> listMenuPage(QueryMenu queryMenu) {
        Page page = MyBatisUtil.page(queryMenu);
        LambdaQueryWrapper<Menu> menuQueryWrapper = new LambdaQueryWrapper<>();
        menuQueryWrapper.eq(Menu::getStatus, GlobalStatusEnum.ENABLE.getStatus());
        if (StringUtils.isNotEmpty(queryMenu.getKeyword())) {
            menuQueryWrapper.like(Menu::getMenuName, queryMenu.getKeyword())
                    .or().like(Menu::getMenuCode, queryMenu.getKeyword());
        }
        IPage<Menu> menuIPage = super.page(page, menuQueryWrapper);

        BaseDTO<MenuInfo> baseDTO = MyBatisUtil.returnPage(menuIPage);
        List<MenuInfo> menuInfos = Lists.newArrayList();
        baseDTO.setData(menuInfos);
        menuIPage.getRecords().forEach(menu -> {
            MenuInfo menuInfo = new MenuInfo();
            BeanUtil.copyProperties(menu, menuInfo);
            menuInfos.add(menuInfo);
        });
        return baseDTO;
    }

    @Override
    public void sortMenu(List<MenuInfo> menuInfos) {
        List<Menu> menus = super.list(new LambdaQueryWrapper<Menu>().in(Menu::getId, menuInfos.stream().map(MenuInfo::getId).collect(Collectors.toList())));
        for (int i = 1; i < menus.size(); i++) {
            if (menus.get(i - 1).getParentId() != menus.get(i).getParentId()) {
                throw new UserBizException(ErrorCodeEnum.USER10014001);
            }
        }
        menus.clear();
        menuInfos.forEach(menuInfo -> {
            Menu menu = new Menu();
            menu.setId(menuInfo.getId());
            menu.setNumber(menuInfo.getNumber());
            menus.add(menu);
        });
        super.updateBatchById(menus);
    }

    @Override
    public List<Map<String, Object>> listMenuTree() {
        List<Menu> menus = super.list();
        if (CollUtil.isEmpty(menus)) {
            return null;
        }
        return tree(menus, MenuConstant.MENU_LEVEL_ROOT.longValue());
    }

    @Override
    public List<Map<String, Object>> tree(List<Menu> menus, Long parentId) {
        //配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名 都要默认值的
        treeNodeConfig.setWeightKey("order");
        // 最大递归深度
        treeNodeConfig.setDeep(3);
        List<Tree<Long>> trees = TreeUtil.build(menus, parentId, treeNodeConfig, (treeNode, tree) -> {
            tree.setId(treeNode.getId());
            tree.setParentId(treeNode.getParentId());
            tree.setWeight(treeNode.getNumber());
            tree.setName(treeNode.getMenuName());
            // 扩展属性 ...
            tree.putExtra("menuName", treeNode.getMenuName());
            tree.putExtra("menuCode", treeNode.getMenuCode());
            tree.putExtra("status", treeNode.getStatus());
            tree.putExtra("url", treeNode.getUrl());
            tree.putExtra("icon", treeNode.getIcon());
            tree.putExtra("parentId", treeNode.getParentId());
            tree.putExtra("component", treeNode.getComponent());
            tree.putExtra("hidden", treeNode.getHidden());
        });
        List<Map<String, Object>> result = Lists.newArrayList();
        trees.forEach(tree -> result.add(Maps.newLinkedHashMap(tree)));
        return result;
    }

    @Override
    public void updateMenuPermission(MenuInfo menuInfo) {
        Preconditions.checkArgument(menuInfo.getId() != null, ErrorCodeEnum.GL10000001.msg());
        Menu menu = super.getById(menuInfo.getId());
        if (menu == null) {
            throw new UserBizException(ErrorCodeEnum.GL10000002, menu.getId());
        }
        // 添加权限
        menuPermissionRelationService.addMenuPermission(menuInfo.getPermissionInfos(), menu.getId());
        refreshMenuPermissionCache(menu.getId());
    }

    public void refreshMenuPermissionCache(Long menuId) {
        List<RoleMenuRelation> roleMenuRelations = roleMenuRelationService.list(Wrappers.lambdaQuery(RoleMenuRelation.class).eq(RoleMenuRelation::getMenuId, menuId));
        if (CollUtil.isNotEmpty(roleMenuRelations)) {
            List<Role> roles = roleService.list(Wrappers.lambdaQuery(Role.class).in(Role::getId, roleMenuRelations.stream().map(RoleMenuRelation::getRoleId).collect(Collectors.toList())));
            roles.forEach(role -> {
                roleService.refreshCache(role.getRoleCode());
            });
        }
    }
}
