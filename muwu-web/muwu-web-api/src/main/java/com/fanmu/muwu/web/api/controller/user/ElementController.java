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
import com.fanmu.muwu.service.provider.user.api.model.dto.element.ElementInfo;
import com.fanmu.muwu.service.provider.user.api.model.query.element.QueryElement;
import com.fanmu.muwu.service.provider.user.api.service.ElementRpcApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 页面元素 前端控制器
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
@RestController
@RequestMapping(value = "/user/element", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "Web - ElementController", tags = {"页面元素"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class ElementController {

    @DubboReference
    ElementRpcApi elementRpcApi;

    @PostMapping(value = "/addElement")
    @ApiOperation(value = "添加页面元素")
    public Wrapper addElement(@RequestBody ElementInfo elementInfo) {
        elementRpcApi.insertElement(elementInfo);
        return WrapMapper.ok();
    }

    @PostMapping(value = "/deleteElement/{elementId}")
    @ApiOperation(value = "删除页面元素")
    public Wrapper deleteElement(@PathVariable("elementId") Long elementId) {
        elementRpcApi.deleteElementById(elementId);
        return WrapMapper.ok();
    }

    @PostMapping(value = "/updateElement")
    @ApiOperation(value = "更新页面元素")
    public Wrapper updateElement(@RequestBody ElementInfo elementInfo) {
        elementRpcApi.updateElement(elementInfo);
        return WrapMapper.ok();
    }

    @PostMapping(value = "/getElement/{elementId}")
    @ApiOperation(value = "获取页面元素")
    public Wrapper<ElementInfo> getElement(@PathVariable("elementId") Long elementId) {
        return WrapMapper.ok(elementRpcApi.getElementById(elementId));
    }

    @PostMapping(value = "/listElement")
    @ApiOperation(value = "列表页面元素")
    public Wrapper<List<ElementInfo>> listElement(@RequestBody(required = false) QueryElement queryElement) {
        return WrapMapper.ok(elementRpcApi.listElement(queryElement));
    }

    @PostMapping(value = "/queryElementsPage")
    @ApiOperation(value = "查询页面元素")
    public Wrapper<BaseDTO<ElementInfo>> queryElementsPage(@RequestBody QueryElement queryElement) {
        return WrapMapper.ok(elementRpcApi.listElementPage(queryElement));
    }

}
