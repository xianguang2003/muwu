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
import com.fanmu.muwu.service.provider.user.api.model.dto.permission.PermissionInfo;
import com.fanmu.muwu.service.provider.user.api.model.query.permission.QueryPermission;
import com.fanmu.muwu.service.provider.user.api.service.PermissionRpcApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 权限 前端控制器
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
@RestController
@RequestMapping(value = "/user/permission", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "Web - PermissionController", tags = {"权限"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class PermissionController {

    @DubboReference
    PermissionRpcApi permissionRpcApi;

    @PostMapping(value = "/addPermission")
    @ApiOperation(value = "添加权限")
    public Wrapper addPermission(@RequestBody PermissionInfo permissionInfo) {
        permissionRpcApi.insertPermission(permissionInfo);
        return WrapMapper.ok();
    }

    @PostMapping(value = "/deletePermission/{permissionId}")
    @ApiOperation(value = "删除权限")
    public Wrapper deletePermission(@PathVariable("permissionId") Long permissionId) {
        permissionRpcApi.deletePermissionById(permissionId);
        return WrapMapper.ok();
    }

    @PostMapping(value = "/updatePermission")
    @ApiOperation(value = "更新权限")
    public Wrapper updatePermission(@RequestBody PermissionInfo permissionInfo) {
        permissionRpcApi.updatePermission(permissionInfo);
        return WrapMapper.ok();
    }

    @PostMapping(value = "/getPermission/{permissionId}")
    @ApiOperation(value = "获取权限")
    public Wrapper<PermissionInfo> getPermission(@PathVariable("permissionId") Long permissionId) {
        return WrapMapper.ok(permissionRpcApi.getPermissionById(permissionId));
    }

    @PostMapping(value = "/listPermission")
    @ApiOperation(value = "列表权限")
    public Wrapper<List<PermissionInfo>> listPermission(@RequestBody(required = false) QueryPermission queryPermission) {
        return WrapMapper.ok(permissionRpcApi.listPermission(queryPermission));
    }

    @PostMapping(value = "/queryPermissionsPage")
    @ApiOperation(value = "查询权限")
    public Wrapper<BaseDTO<PermissionInfo>> queryPermissionsPage(@RequestBody QueryPermission queryPermission) {
        return WrapMapper.ok(permissionRpcApi.listPermissionPage(queryPermission));
    }

    @PostMapping(value = "/refreshCache")
    @ApiOperation(value = "刷新缓存")
    public Wrapper refreshCache() {
        permissionRpcApi.refreshCache();
        return WrapMapper.ok();
    }

    @PostMapping(value = "/batchUpdateWhitelist")
    @ApiOperation(value = "批量更新白名单")
    public Wrapper batchUpdateWhitelist(@RequestBody List<PermissionInfo> permissionInfos) {
        permissionRpcApi.batchUpdateWhitelist(permissionInfos);
        return WrapMapper.ok();
    }

    @PostMapping(value = "/batchUpdateRoleSharePermission")
    @ApiOperation(value = "批量更新角色共享权限")
    public Wrapper batchUpdateRoleSharePermission(@RequestBody List<PermissionInfo> permissionInfos) {
        permissionRpcApi.batchUpdateRoleSharePermission(permissionInfos);
        return WrapMapper.ok();
    }
}
