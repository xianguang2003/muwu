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
import com.fanmu.muwu.service.provider.user.api.model.dto.role.RoleInfo;
import com.fanmu.muwu.service.provider.user.api.model.query.role.QueryRole;
import com.fanmu.muwu.service.provider.user.api.service.RoleRpcApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 角色信息 前端控制器
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
@RestController
@RequestMapping(value = "/user/role", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "Web - RoleController", tags = {"角色信息"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class RoleController {

    @DubboReference
    RoleRpcApi roleRpcApi;

    @PostMapping(value = "/addRole")
    @ApiOperation(value = "添加角色信息")
    public Wrapper addRole(@RequestBody RoleInfo roleInfo) {
        roleRpcApi.insertRole(roleInfo);
        return WrapMapper.ok();
    }

    @PostMapping(value = "/deleteRole/{roleId}")
    @ApiOperation(value = "删除角色信息")
    public Wrapper deleteRole(@PathVariable("roleId") Long roleId) {
        roleRpcApi.deleteRoleById(roleId);
        return WrapMapper.ok();
    }

    @PostMapping(value = "/updateRole")
    @ApiOperation(value = "更新角色信息")
    public Wrapper updateRole(@RequestBody RoleInfo roleInfo) {
        roleRpcApi.updateRole(roleInfo);
        return WrapMapper.ok();
    }

    @PostMapping(value = "/getRole/{roleId}")
    @ApiOperation(value = "获取角色信息")
    public Wrapper<RoleInfo> getRole(@PathVariable("roleId") Long roleId) {
        return WrapMapper.ok(roleRpcApi.getRoleById(roleId));
    }

    @PostMapping(value = "/listRole")
    @ApiOperation(value = "列表角色信息")
    public Wrapper<List<RoleInfo>> listRole(@RequestBody(required = false) QueryRole queryRole) {
        return WrapMapper.ok(roleRpcApi.listRole(queryRole));
    }

    @PostMapping(value = "/queryRolesPage")
    @ApiOperation(value = "查询角色信息")
    public Wrapper<BaseDTO<RoleInfo>> queryRolesPage(@RequestBody QueryRole queryRole) {
        return WrapMapper.ok(roleRpcApi.listRolePage(queryRole));
    }

    @PostMapping(value = "/saveRoleMenu")
    @ApiOperation(value = "修改角色菜单")
    public Wrapper saveRoleMenu(@RequestBody RoleInfo roleInfo) {
        roleRpcApi.saveRoleMenu(roleInfo);
        return WrapMapper.ok();
    }

    @PostMapping(value = "/updateRolePermission")
    @ApiOperation(value = "更新角色权限信息")
    public Wrapper updateRolePermission(@RequestBody RoleInfo roleInfo) {
        roleRpcApi.updateRolePermission(roleInfo);
        return WrapMapper.ok();
    }

    @PostMapping(value = "/refreshCache")
    @ApiOperation(value = "刷新缓存")
    public Wrapper refreshCache() {
        roleRpcApi.refreshCache();
        return WrapMapper.ok();
    }

}
