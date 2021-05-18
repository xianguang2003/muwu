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
import com.fanmu.muwu.service.provider.user.api.model.dto.socialuser.SocialUserInfo;
import com.fanmu.muwu.service.provider.user.api.model.query.socialuser.QuerySocialUser;
import com.fanmu.muwu.service.provider.user.api.service.SocialUserRpcApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 社交用户信息 前端控制器
 * </p>
 *
 * @author mumu
 * @since 2020-08-11
 */
@RestController
@RequestMapping(value = "/user/socialUser", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "Web - SocialUserController", tags = {"社交用户信息"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class SocialUserController {

    @DubboReference
    SocialUserRpcApi socialUserRpcApi;

    @PostMapping(value = "/addSocialUser")
    @ApiOperation(value = "添加社交用户信息")
    public Wrapper addSocialUser(@RequestBody SocialUserInfo socialUserInfo) {
        socialUserRpcApi.insertSocialUser(socialUserInfo);
        return WrapMapper.ok();
    }

    @PostMapping(value = "/deleteSocialUser/{socialUserId}")
    @ApiOperation(value = "删除社交用户信息")
    public Wrapper deleteSocialUser(@PathVariable("socialUserId") Long socialUserId) {
        socialUserRpcApi.deleteSocialUserById(socialUserId);
        return WrapMapper.ok();
    }

    @PostMapping(value = "/updateSocialUser")
    @ApiOperation(value = "更新社交用户信息")
    public Wrapper updateSocialUser(@RequestBody SocialUserInfo socialUserInfo) {
        socialUserRpcApi.updateSocialUser(socialUserInfo);
        return WrapMapper.ok();
    }

    @PostMapping(value = "/getSocialUser/{socialUserId}")
    @ApiOperation(value = "获取社交用户信息")
    public Wrapper<SocialUserInfo> getSocialUser(@PathVariable("socialUserId") Long socialUserId) {
        return WrapMapper.ok(socialUserRpcApi.getSocialUserById(socialUserId));
    }

    @PostMapping(value = "/listSocialUser")
    @ApiOperation(value = "列表社交用户信息")
    public Wrapper<List<SocialUserInfo>> listSocialUser() {
        return WrapMapper.ok(socialUserRpcApi.listSocialUser());
    }

    @PostMapping(value = "/querySocialUsersPage")
    @ApiOperation(value = "查询社交用户信息")
    public Wrapper<BaseDTO<SocialUserInfo>> querySocialUsersPage(@RequestBody QuerySocialUser querySocialUser) {
        return WrapMapper.ok(socialUserRpcApi.listSocialUserPage(querySocialUser));
    }

}
