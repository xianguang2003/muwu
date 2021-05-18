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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanmu.muwu.common.base.enums.ErrorCodeEnum;
import com.fanmu.muwu.common.base.enums.GlobalStatusEnum;
import com.fanmu.muwu.common.base.pojo.BaseDTO;
import com.fanmu.muwu.common.coer.extension.service.impl.ServiceImpl;
import com.fanmu.muwu.common.coer.util.MyBatisUtil;
import com.fanmu.muwu.service.provider.user.api.constant.MenuConstant;
import com.fanmu.muwu.service.provider.user.api.exceptions.UserBizException;
import com.fanmu.muwu.service.provider.user.api.model.dto.role.RoleInfo;
import com.fanmu.muwu.service.provider.user.api.model.dto.user.AuthUser;
import com.fanmu.muwu.service.provider.user.api.model.dto.user.UserInfo;
import com.fanmu.muwu.service.provider.user.api.model.query.user.QueryUser;
import com.fanmu.muwu.service.provider.user.api.service.UserRpcApi;
import com.fanmu.muwu.service.provider.user.mapper.UserMapper;
import com.fanmu.muwu.service.provider.user.model.domain.*;
import com.fanmu.muwu.service.provider.user.service.*;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户信息 服务实现类
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
@DubboService(interfaceClass = UserRpcApi.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    RoleService roleService;

    @Autowired
    UserRoleRelationService userRoleRelationService;

    @Autowired
    MenuService menuService;

    @Autowired
    RoleMenuRelationService roleMenuRelationService;

    @Autowired
    UserGroupUserRelationService userGroupUserRelationService;

    @Override
    public void insertUser(UserInfo userInfo) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(userInfo.getUsername()), ErrorCodeEnum.USER10011001.msg());
        Preconditions.checkArgument(StringUtils.isNotEmpty(userInfo.getPassword()), ErrorCodeEnum.USER10011002.msg());

        // 用户名
        User user = super.getOne(new QueryWrapper<User>().lambda().eq(User::getUsername, userInfo.getUsername()));
        if (user != null) {
            throw new UserBizException(ErrorCodeEnum.USER10011013, userInfo.getUsername());
        }
        // 手机号
        if (StringUtils.isNotEmpty(userInfo.getMobile())) {
            user = super.getOne(new QueryWrapper<User>().lambda().eq(User::getMobile, userInfo.getMobile()));
            if (user != null) {
                throw new UserBizException(ErrorCodeEnum.USER10011014, userInfo.getId());
            }
        }
        // 邮箱
        if (StringUtils.isNotEmpty(userInfo.getEmail())) {
            user = super.getOne(new QueryWrapper<User>().lambda().eq(User::getEmail, userInfo.getEmail()));
            if (user != null) {
                throw new UserBizException(ErrorCodeEnum.USER10011015, userInfo.getId());
            }
        }

        user = new User();
        BeanUtil.copyProperties(userInfo, user);
        super.save(user);
        userInfo.setId(user.getId());
        // 添加角色
        if (userInfo.getRoleInfos() != null) {
            userRoleRelationService.addUserRole(userInfo.getRoleInfos(), user.getId());
        }
    }

    @Override
    public void deleteUserById(Long id) {
        Preconditions.checkArgument(id != null, ErrorCodeEnum.USER10011016.msg());

        User user = super.getById(id);
        if (user == null) {
            throw new UserBizException(ErrorCodeEnum.USER10011017, id);
        }
        super.removeById(id);
        userRoleRelationService.remove(new QueryWrapper<UserRoleRelation>().lambda().eq(UserRoleRelation::getUserId, id));
        userGroupUserRelationService.remove(new QueryWrapper<UserGroupUserRelation>().lambda().eq(UserGroupUserRelation::getUserId, id));
    }

    @Override
    public void updateUser(UserInfo userInfo) {
        Preconditions.checkArgument(userInfo.getId() != null, ErrorCodeEnum.USER10011016.msg());

        User user = super.getById(userInfo.getId());
        if (user == null) {
            throw new UserBizException(ErrorCodeEnum.USER10011017, userInfo.getId());
        }
        // 用户名
        if (StringUtils.isNotEmpty(userInfo.getUsername())) {
            User userTmp = super.getOne(new QueryWrapper<User>().lambda().ne(User::getId, user.getId())
                    .and(i -> i.eq(User::getUsername, userInfo.getUsername())));
            if (userTmp != null) {
                throw new UserBizException(ErrorCodeEnum.USER10011013);
            }
        }
        // 手机号
        if (StringUtils.isNotEmpty(userInfo.getMobile())) {
            User userTmp = super.getOne(new QueryWrapper<User>().lambda().ne(User::getId, user.getId())
                    .and(i -> i.eq(User::getMobile, userInfo.getMobile())));
            if (userTmp != null) {
                throw new UserBizException(ErrorCodeEnum.USER10011014);
            }
        }
        // 邮箱
        if (StringUtils.isNotEmpty(userInfo.getEmail())) {
            User userTmp = super.getOne(new QueryWrapper<User>().lambda().ne(User::getId, user.getId())
                    .and(i -> i.eq(User::getEmail, userInfo.getEmail())));
            if (userTmp != null) {
                throw new UserBizException(ErrorCodeEnum.USER10011015);
            }
        }
        // 添加角色
        userRoleRelationService.addUserRole(userInfo.getRoleInfos(), user.getId());
        // 修改数据
        user = new User();
        BeanUtil.copyProperties(userInfo, user);
        super.updateById(user);
    }

    @Override
    public UserInfo getUserById(Long id) {
        User user = super.getById(id);
        if (user == null) {
            throw new UserBizException(ErrorCodeEnum.USER10011017, id);
        }
        List<UserRoleRelation> userRoleRelations = userRoleRelationService.list(new LambdaQueryWrapper<UserRoleRelation>()
                .eq(UserRoleRelation::getUserId, id));
        List<RoleInfo> roleInfos = Lists.newArrayList();
        if (CollUtil.isNotEmpty(userRoleRelations)) {
            List<Role> roles = roleService.list(new QueryWrapper<Role>().lambda()
                    .in(Role::getId, userRoleRelations.stream().map(UserRoleRelation::getRoleId).collect(Collectors.toList())));
            roles.forEach(role -> {
                RoleInfo roleInfo = new RoleInfo();
                BeanUtil.copyProperties(role, roleInfo);
                roleInfos.add(roleInfo);
            });
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setRoleInfos(roleInfos);
        BeanUtil.copyProperties(user, userInfo);
        return userInfo;
    }

    @Override
    public List<UserInfo> listUser() {
        return listUser(null);
    }

    @Override
    public List<UserInfo> listUser(QueryUser queryUser) {
        LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper();
        userQueryWrapper.eq(User::getStatus, GlobalStatusEnum.ENABLE.getStatus());
        if (queryUser != null) {
            if (StringUtils.isNotEmpty(queryUser.getKeyword())) {
                userQueryWrapper.like(User::getUsername, queryUser.getKeyword())
                        .or().like(User::getNickname, queryUser.getKeyword());
            }
            if (CollUtil.isNotEmpty(queryUser.getIds())) {
                userQueryWrapper.in(User::getId, queryUser.getIds());
            }
        }
        List<User> users = super.list(userQueryWrapper);
        List<UserInfo> userInfos = Lists.newArrayList();
        users.forEach(user -> {
            UserInfo userInfo = new UserInfo();
            BeanUtil.copyProperties(user, userInfo);
            userInfos.add(userInfo);
        });
        return userInfos;
    }

    @Override
    public BaseDTO<UserInfo> listUserPage(QueryUser queryUser) {
        Page page = MyBatisUtil.page(queryUser);
        LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper();
        userQueryWrapper.eq(User::getStatus, GlobalStatusEnum.ENABLE.getStatus());
        if (StringUtils.isNotEmpty(queryUser.getKeyword())) {
            userQueryWrapper.like(User::getUsername, queryUser.getKeyword())
                    .or().like(User::getNickname, queryUser.getKeyword());
        }
        IPage<User> userIPage = super.page(page, userQueryWrapper);

        BaseDTO<UserInfo> baseDTO = MyBatisUtil.returnPage(userIPage);
        List<UserInfo> userInfos = Lists.newArrayList();
        baseDTO.setData(userInfos);
        userIPage.getRecords().forEach(user -> {
            UserInfo userInfo = new UserInfo();
            BeanUtil.copyProperties(user, userInfo);
            userInfos.add(userInfo);
        });
        return baseDTO;
    }

    @Override
    public AuthUser getAuthUser(String username) {
        User user = super.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username)
                .or().eq(User::getMobile, username).or().eq(User::getEmail, username));
        if (user == null) {
            throw new UserBizException(ErrorCodeEnum.USER10011017, username);
        }

        List<UserRoleRelation> userRoleRelations = userRoleRelationService.list(new QueryWrapper<UserRoleRelation>().lambda().eq(UserRoleRelation::getUserId, user.getId()));
        List<Role> roles = roleService.list(new QueryWrapper<Role>().lambda().in(Role::getId, userRoleRelations.stream().map(UserRoleRelation::getRoleId).collect(Collectors.toList())));
        AuthUser authUser = new AuthUser();
        authUser.setUsername(user.getUsername());
        authUser.setPassword(user.getPassword());
        authUser.setNickname(user.getNickname());
        authUser.setUserId(user.getId());
        authUser.setRoles(roles.stream().map(Role::getRoleCode).collect(Collectors.toList()));
        return authUser;
    }

    @Override
    public List<Map<String, Object>> listUserTreeById(Long id) {

        Preconditions.checkArgument(id != null, ErrorCodeEnum.USER10011018.msg());

        User user = super.getById(id);
        if (user == null) {
            throw new UserBizException(ErrorCodeEnum.USER10011017, id);
        }

        List<UserRoleRelation> userRoleRelationList = userRoleRelationService.list(new QueryWrapper<UserRoleRelation>().lambda().eq(UserRoleRelation::getUserId, user.getId()));
        if (CollUtil.isEmpty(userRoleRelationList)) {
            return null;
        }
        List<Role> roles = roleService.list(new LambdaQueryWrapper<Role>()
                .eq(Role::getStatus, GlobalStatusEnum.ENABLE.getStatus())
                .in(Role::getId, userRoleRelationList.stream().map(UserRoleRelation::getRoleId).collect(Collectors.toList())));
        if (CollUtil.isEmpty(roles)) {
            return null;
        }
        List<RoleMenuRelation> roleMenuRelations = roleMenuRelationService.list(new QueryWrapper<RoleMenuRelation>().lambda().in(RoleMenuRelation::getRoleId, roles.stream().map(Role::getId).collect(Collectors.toList())));
        if (CollUtil.isEmpty(roleMenuRelations)) {
            return null;
        }
        List<Menu> menus = menuService.list(new QueryWrapper<Menu>().lambda()
                .eq(Menu::getStatus, GlobalStatusEnum.ENABLE.getStatus())
                .in(Menu::getId, roleMenuRelations.stream().map(RoleMenuRelation::getMenuId).collect(Collectors.toList())));
        if (CollUtil.isEmpty(menus)) {
            return null;
        }
        return menuService.tree(menus, MenuConstant.MENU_LEVEL_FIRST.longValue());
    }

    @Override
    public void saveUserRole(UserInfo userInfo) {
        List<RoleInfo> roleInfos = userInfo.getRoleInfos();
        if (CollUtil.isEmpty(roleInfos)) {
            throw new UserBizException(ErrorCodeEnum.USER10012001);
        }
        List<Role> roles = roleService.list(new LambdaQueryWrapper<Role>()
                .eq(Role::getStatus, GlobalStatusEnum.ENABLE.getStatus())
                .in(Role::getId, roleInfos.stream().map(RoleInfo::getId).collect(Collectors.toList())));
        if (CollUtil.isEmpty(roles)) {
            throw new UserBizException(ErrorCodeEnum.USER10012001);
        }
        // 删除角色ID
        userRoleRelationService.remove(new QueryWrapper<UserRoleRelation>().lambda().eq(UserRoleRelation::getUserId, userInfo.getId()));
        // 新增角色ID
        List<UserRoleRelation> userRoleRelations = Lists.newArrayList();
        for (Role role : roles) {
            UserRoleRelation userRoleRelation = new UserRoleRelation();
            userRoleRelation.setUserId(userInfo.getId());
            userRoleRelation.setRoleId(role.getId());
            userRoleRelations.add(userRoleRelation);
        }
        userRoleRelationService.saveBatch(userRoleRelations);
    }

    @Override
    public void resetUsertPassword(UserInfo userInfo) {
        User user = super.getById(userInfo.getId());
        if (user == null) {
            throw new UserBizException(ErrorCodeEnum.USER10011017, userInfo.getId());
        }
        user = new User();
        user.setId(userInfo.getId());
        user.setPassword(userInfo.getPassword());
        super.updateById(user);
    }

}
