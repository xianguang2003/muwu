/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.web.api.controller.user;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.fanmu.muwu.common.base.pojo.BaseDTO;
import com.fanmu.muwu.common.web.extension.wrapper.WrapMapper;
import com.fanmu.muwu.common.web.extension.wrapper.Wrapper;
import com.fanmu.muwu.service.provider.user.api.model.dto.permission.PermissionInfo;
import com.fanmu.muwu.service.provider.user.api.service.PermissionCategoryRpcApi;
import com.fanmu.muwu.service.provider.user.api.model.dto.permissioncategory.PermissionCategoryInfo;
import com.fanmu.muwu.service.provider.user.api.model.query.permissioncategory.QueryPermissionCategory;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ApiListing;
import springfox.documentation.service.Documentation;
import springfox.documentation.service.Tag;
import springfox.documentation.spring.web.DocumentationCache;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 权限类别 前端控制器
 * </p>
 *
 * @author mumu
 * @since 2021-03-24
 */
@RestController
@RequestMapping(value = "/user/permissionCategory", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "Web - PermissionCategoryController", tags = {"权限类别"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class PermissionCategoryController {

    @DubboReference
    PermissionCategoryRpcApi permissionCategoryRpcApi;

    @Autowired
    DocumentationCache documentationCache;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @PostMapping(value = "/addPermissionCategory")
    @ApiOperation(value = "添加权限类别")
    public Wrapper addPermissionCategory(@RequestBody PermissionCategoryInfo permissionCategoryInfo) {
        permissionCategoryRpcApi.insertPermissionCategory(permissionCategoryInfo);
        return WrapMapper.ok();
    }

    @PostMapping(value = "/deletePermissionCategory/{permissionCategoryId}")
    @ApiOperation(value = "删除权限类别")
    public Wrapper deletePermissionCategory(@PathVariable("permissionCategoryId") Long permissionCategoryId) {
        permissionCategoryRpcApi.deletePermissionCategoryById(permissionCategoryId);
        return WrapMapper.ok();
    }

    @PostMapping(value = "/updatePermissionCategory")
    @ApiOperation(value = "更新权限类别")
    public Wrapper updatePermissionCategory(@RequestBody PermissionCategoryInfo permissionCategoryInfo) {
        permissionCategoryRpcApi.updatePermissionCategory(permissionCategoryInfo);
        return WrapMapper.ok();
    }

    @PostMapping(value = "/getPermissionCategory/{permissionCategoryId}")
    @ApiOperation(value = "获取权限类别")
    public Wrapper<PermissionCategoryInfo> getPermissionCategory(@PathVariable("permissionCategoryId") Long permissionCategoryId) {
        return WrapMapper.ok(permissionCategoryRpcApi.getPermissionCategoryById(permissionCategoryId));
    }

    @PostMapping(value = "/listPermissionCategory")
    @ApiOperation(value = "列表权限类别")
    public Wrapper<List<PermissionCategoryInfo>> listPermissionCategory(@RequestBody(required = false) QueryPermissionCategory queryPermissionCategory) {
        return WrapMapper.ok(permissionCategoryRpcApi.listPermissionCategory(queryPermissionCategory));
    }

    @PostMapping(value = "/queryPermissionCategorysPage")
    @ApiOperation(value = "查询权限类别")
    public Wrapper<BaseDTO<PermissionCategoryInfo>> queryPermissionCategorysPage(@RequestBody QueryPermissionCategory queryPermissionCategory) {
        return WrapMapper.ok(permissionCategoryRpcApi.listPermissionCategoryPage(queryPermissionCategory));
    }

    @PostMapping(value = "/generatePermission")
    @ApiOperation(value = "构建权限列表")
    public Wrapper generatePermission() {
        List<PermissionCategoryInfo> permissionCategoryInfos = Lists.newArrayList();
        int contextPathLength = contextPath.length();
        Documentation documentation = documentationCache.documentationByGroup("default");
        Map<String, List<ApiListing>> apiListings = documentation.getApiListings();
        Collection<List<ApiListing>> values = apiListings.values();
        for (List<ApiListing> value : values) {
            for (ApiListing apiListing : value) {
                Tag tag = CollUtil.getFirst(apiListing.getTags());
                PermissionCategoryInfo permissionCategoryInfo = new PermissionCategoryInfo();
                permissionCategoryInfos.add(permissionCategoryInfo);
                permissionCategoryInfo.setCategoryName(tag.getName());
                List<PermissionInfo> permissionInfos = Lists.newArrayList();
                permissionCategoryInfo.setPermissionInfos(permissionInfos);
                List<ApiDescription> apis = apiListing.getApis();
                for (ApiDescription api : apis) {
                    String summary = api.getOperations().get(0).getSummary();
                    String path = api.getPath();
                    if (path.contains("{")) {
                        path = path.substring(0, path.indexOf("{")) + "*";
                    }
//                    path = path.substring(contextPathLength);
                    String code = api.getPath().substring(1).replace("/", ":");
                    if (code.contains("{")) {
                        code = code.substring(0, code.indexOf("{") - 1);
                    }
                    code = code.substring(contextPathLength);
                    PermissionInfo permissionInfo = new PermissionInfo();
                    permissionInfo.setPermissionName(summary);
                    permissionInfo.setPermissionCode(code);
                    permissionInfo.setUrl(path);
                    permissionInfos.add(permissionInfo);
                }
            }
        }
        permissionCategoryRpcApi.generatePermission(permissionCategoryInfos);
        return WrapMapper.ok();
    }
}
