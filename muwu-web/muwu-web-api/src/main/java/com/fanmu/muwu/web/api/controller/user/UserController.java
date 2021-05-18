/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.web.api.controller.user;

import com.fanmu.muwu.common.base.pojo.BaseDTO;
import com.fanmu.muwu.common.security.SecurityUtils;
import com.fanmu.muwu.common.web.extension.wrapper.WrapMapper;
import com.fanmu.muwu.common.web.extension.wrapper.Wrapper;
import com.fanmu.muwu.service.provider.user.api.model.dto.user.UserInfo;
import com.fanmu.muwu.service.provider.user.api.model.query.user.QueryUser;
import com.fanmu.muwu.service.provider.user.api.service.UserRpcApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户信息 前端控制器
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
@RestController
@RequestMapping(value = "/user/user", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "Web - UserController", tags = {"用户信息"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @DubboReference
    UserRpcApi userRpcApi;

    @PostMapping(value = "/addUser")
    @ApiOperation(value = "添加用户信息")
    public Wrapper addUser(@RequestBody UserInfo userInfo) {
        userRpcApi.insertUser(userInfo);
        return WrapMapper.ok();
    }

    @PostMapping(value = "/deleteUser/{userId}")
    @ApiOperation(value = "删除用户信息")
    public Wrapper deleteUser(@PathVariable("userId") Long userId) {
        userRpcApi.deleteUserById(userId);
        return WrapMapper.ok();
    }

    @PostMapping(value = "/updateUser")
    @ApiOperation(value = "更新用户信息")
    public Wrapper updateUser(@RequestBody UserInfo userInfo) {
        userRpcApi.updateUser(userInfo);
        return WrapMapper.ok();
    }

    @PostMapping(value = "/getUser/{userId}")
    @ApiOperation(value = "获取用户信息")
    public Wrapper<UserInfo> getUser(@PathVariable("userId") Long userId) {
        return WrapMapper.ok(userRpcApi.getUserById(userId));
    }

    @PostMapping(value = "/listUser")
    @ApiOperation(value = "列表用户信息")
    public Wrapper<List<UserInfo>> listUser(@RequestBody(required = false) QueryUser queryUser) {
        return WrapMapper.ok(userRpcApi.listUser(queryUser));
    }

    @PostMapping(value = "/queryUsersPage")
    @ApiOperation(value = "查询用户信息")
    public Wrapper<BaseDTO<UserInfo>> queryUsersPage(@RequestBody QueryUser queryUser) {
        return WrapMapper.ok(userRpcApi.listUserPage(queryUser));
    }

    @PostMapping(value = "/getUserTree")
    @ApiOperation(value = "查询用户")
    public Wrapper getUserTree() {
        return WrapMapper.ok(userRpcApi.listUserTreeById(SecurityUtils.getCurrentUserId()));
    }

    @PostMapping(value = "/saveUserRole")
    @ApiOperation(value = "修改用户角色")
    public Wrapper saveUserRole(@RequestBody UserInfo userInfo) {
        userRpcApi.saveUserRole(userInfo);
        return WrapMapper.ok();
    }

    @PostMapping(value = "/resetUsertPassword")
    @ApiOperation(value = "重置用户")
    public Wrapper resetUsertPassword(@RequestBody UserInfo userInfo) {
        if (StringUtils.isNotEmpty(userInfo.getPassword())) {
            userInfo.setPassword(SecurityUtils.passwordEncoder(userInfo.getPassword()));
        }
        userRpcApi.resetUsertPassword(userInfo);
        return WrapMapper.ok();
    }

}
