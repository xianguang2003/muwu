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
import com.fanmu.muwu.service.provider.user.api.model.dto.dictitem.DictItemInfo;
import com.fanmu.muwu.service.provider.user.api.model.query.dictitem.QueryDictItem;
import com.fanmu.muwu.service.provider.user.api.service.DictItemRpcApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 字典项表 前端控制器
 * </p>
 *
 * @author mumu
 * @since 2020-10-25
 */
@RestController
@RequestMapping(value = "/user/dictItem", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "Web - DictItemController", tags = {"字典项表"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class DictItemController {

    @DubboReference
    DictItemRpcApi dictItemRpcApi;

    @PostMapping(value = "/addDictItem")
    @ApiOperation(value = "添加字典项表")
    public Wrapper addDictItem(@RequestBody DictItemInfo dictItemInfo) {
        dictItemRpcApi.insertDictItem(dictItemInfo);
        return WrapMapper.ok();
    }

    @PostMapping(value = "/deleteDictItem/{dictItemId}")
    @ApiOperation(value = "删除字典项表")
    public Wrapper deleteDictItem(@PathVariable("dictItemId") Long dictItemId) {
        dictItemRpcApi.deleteDictItemById(dictItemId);
        return WrapMapper.ok();
    }

    @PostMapping(value = "/updateDictItem")
    @ApiOperation(value = "更新字典项表")
    public Wrapper updateDictItem(@RequestBody DictItemInfo dictItemInfo) {
        dictItemRpcApi.updateDictItem(dictItemInfo);
        return WrapMapper.ok();
    }

    @PostMapping(value = "/getDictItem/{dictItemId}")
    @ApiOperation(value = "获取字典项表")
    public Wrapper<DictItemInfo> getDictItem(@PathVariable("dictItemId") Long dictItemId) {
        return WrapMapper.ok(dictItemRpcApi.getDictItemById(dictItemId));
    }

    @PostMapping(value = "/listDictItem")
    @ApiOperation(value = "列表字典项表")
    public Wrapper<List<DictItemInfo>> listDictItem(@RequestBody(required = false) QueryDictItem queryDictItem) {
        return WrapMapper.ok(dictItemRpcApi.listDictItem(queryDictItem));
    }

    @PostMapping(value = "/queryDictItemsPage")
    @ApiOperation(value = "查询字典项表")
    public Wrapper<BaseDTO<DictItemInfo>> queryDictItemsPage(@RequestBody QueryDictItem queryDictItem) {
        return WrapMapper.ok(dictItemRpcApi.listDictItemPage(queryDictItem));
    }

}
