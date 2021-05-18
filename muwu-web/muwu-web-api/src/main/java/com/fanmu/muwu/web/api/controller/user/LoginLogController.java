/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.web.api.controller.user;

import com.fanmu.muwu.common.web.extension.wrapper.WrapMapper;
import com.fanmu.muwu.common.web.extension.wrapper.Wrapper;
import com.fanmu.muwu.service.provider.user.api.model.dto.loginlog.LoginLogStatistics;
import com.fanmu.muwu.service.provider.user.api.service.LoginLogRpcApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 登录日志统计
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
@RestController
@RequestMapping(value = "/user/loginLog", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "Web - ElementController", tags = {"登录日志统计"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginLogController {

    @DubboReference
    LoginLogRpcApi loginLogRpcApi;

    @PostMapping(value = "/statisticsData")
    @ApiOperation(value = "统计数据")
    public Wrapper<LoginLogStatistics> statisticsData() {
        return WrapMapper.ok(loginLogRpcApi.statisticsData());
    }

}
