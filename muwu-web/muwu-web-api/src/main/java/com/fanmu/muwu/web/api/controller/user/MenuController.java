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
import com.fanmu.muwu.service.provider.user.api.model.dto.menu.MenuInfo;
import com.fanmu.muwu.service.provider.user.api.model.query.menu.QueryMenu;
import com.fanmu.muwu.service.provider.user.api.service.MenuRpcApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜单 前端控制器
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
@RestController
@RequestMapping(value = "/user/menu", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "Web - MenuController", tags = {"菜单"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuController {

    @DubboReference
    MenuRpcApi menuRpcApi;

    @PostMapping(value = "/addMenu")
    @ApiOperation(value = "添加菜单")
    public Wrapper addMenu(@RequestBody MenuInfo menuInfo) {
        menuRpcApi.insertMenu(menuInfo);
        return WrapMapper.ok();
    }

    @PostMapping(value = "/deleteMenu/{menuId}")
    @ApiOperation(value = "删除菜单")
    public Wrapper deleteMenu(@PathVariable("menuId") Long menuId) {
        menuRpcApi.deleteMenuById(menuId);
        return WrapMapper.ok();
    }

    @PostMapping(value = "/updateMenu")
    @ApiOperation(value = "更新菜单")
    public Wrapper updateMenu(@RequestBody MenuInfo menuInfo) {
        menuRpcApi.updateMenu(menuInfo);
        return WrapMapper.ok();
    }

    @PostMapping(value = "/getMenu/{menuId}")
    @ApiOperation(value = "获取菜单")
    public Wrapper<MenuInfo> getMenu(@PathVariable("menuId") Long menuId) {
        return WrapMapper.ok(menuRpcApi.getMenuById(menuId));
    }

    @PostMapping(value = "/listMenu")
    @ApiOperation(value = "列表菜单")
    public Wrapper<List<MenuInfo>> listMenu(@RequestBody(required = false) QueryMenu queryMenu) {
        return WrapMapper.ok(menuRpcApi.listMenu(queryMenu));
    }

    @PostMapping(value = "/queryMenusPage")
    @ApiOperation(value = "查询菜单")
    public Wrapper<BaseDTO<MenuInfo>> queryMenusPage(@RequestBody QueryMenu queryMenu) {
        return WrapMapper.ok(menuRpcApi.listMenuPage(queryMenu));
    }

    @PostMapping(value = "/listMenuTree")
    @ApiOperation(value = "查询菜单")
    public Wrapper listMenuTree() {
        return WrapMapper.ok(menuRpcApi.listMenuTree());
    }

    @PostMapping(value = "/sortMenu")
    @ApiOperation(value = "排序菜单")
    public Wrapper sortMenu(@RequestBody List<MenuInfo> menuInfos) {
        menuRpcApi.sortMenu(menuInfos);
        return WrapMapper.ok();
    }

    @PostMapping(value = "/updateMenuPermission")
    @ApiOperation(value = "更新菜单权限信息")
    public Wrapper updateMenuPermission(@RequestBody MenuInfo menuInfo) {
        menuRpcApi.updateMenuPermission(menuInfo);
        return WrapMapper.ok();
    }

}
