/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.web.api.controller.user;

import com.fanmu.muwu.common.base.pojo.BaseDTO;
import com.fanmu.muwu.common.web.extension.wrapper.WrapMapper;
import com.fanmu.muwu.common.web.extension.wrapper.Wrapper;
import com.fanmu.muwu.service.provider.user.api.model.dto.usergroup.UserGroupInfo;
import com.fanmu.muwu.service.provider.user.api.model.query.usergroup.QueryUserGroup;
import com.fanmu.muwu.service.provider.user.api.service.UserGroupRpcApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户组 前端控制器
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
@RestController
@RequestMapping(value = "/user/userGroup", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "Web - UserGroupController", tags = {"用户组"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserGroupController {

    @DubboReference
    UserGroupRpcApi userGroupRpcApi;

    @PostMapping(value = "/addUserGroup")
    @ApiOperation(value = "添加用户组")
    public Wrapper addUserGroup(@RequestBody UserGroupInfo userGroupInfo) {
        userGroupRpcApi.insertUserGroup(userGroupInfo);
        return WrapMapper.ok();
    }

    @PostMapping(value = "/deleteUserGroup/{userGroupId}")
    @ApiOperation(value = "删除用户组")
    public Wrapper deleteUserGroup(@PathVariable("userGroupId") Long userGroupId) {
        userGroupRpcApi.deleteUserGroupById(userGroupId);
        return WrapMapper.ok();
    }

    @PostMapping(value = "/updateUserGroup")
    @ApiOperation(value = "更新用户组")
    public Wrapper updateUserGroup(@RequestBody UserGroupInfo userGroupInfo) {
        userGroupRpcApi.updateUserGroup(userGroupInfo);
        return WrapMapper.ok();
    }

    @PostMapping(value = "/getUserGroup/{userGroupId}")
    @ApiOperation(value = "获取用户组")
    public Wrapper<UserGroupInfo> getUserGroup(@PathVariable("userGroupId") Long userGroupId) {
        return WrapMapper.ok(userGroupRpcApi.getUserGroupById(userGroupId));
    }

    @PostMapping(value = "/listUserGroup")
    @ApiOperation(value = "列表用户组")
    public Wrapper<List<UserGroupInfo>> listUserGroup(@RequestBody(required = false) QueryUserGroup queryUserGroup) {
        return WrapMapper.ok(userGroupRpcApi.listUserGroup(queryUserGroup));
    }

    @PostMapping(value = "/queryUserGroupsPage")
    @ApiOperation(value = "查询用户组")
    public Wrapper<BaseDTO<UserGroupInfo>> queryUserGroupsPage(@RequestBody QueryUserGroup queryUserGroup) {
        return WrapMapper.ok(userGroupRpcApi.listUserGroupPage(queryUserGroup));
    }

}
