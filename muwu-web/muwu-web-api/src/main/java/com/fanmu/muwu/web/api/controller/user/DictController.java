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
import com.fanmu.muwu.service.provider.user.api.model.dto.dict.DictInfo;
import com.fanmu.muwu.service.provider.user.api.model.dto.dictitem.DictItemLabelValue;
import com.fanmu.muwu.service.provider.user.api.model.query.dict.QueryDict;
import com.fanmu.muwu.service.provider.user.api.service.DictRpcApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 字典 前端控制器
 * </p>
 *
 * @author mumu
 * @since 2020-10-25
 */
@RestController
@RequestMapping(value = "/user/dict", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "Web - DictController", tags = {"字典"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class DictController {

    @DubboReference
    DictRpcApi dictRpcApi;

    @PostMapping(value = "/addDict")
    @ApiOperation(value = "添加字典")
    public Wrapper addDict(@RequestBody DictInfo dictInfo) {
        dictRpcApi.insertDict(dictInfo);
        return WrapMapper.ok();
    }

    @PostMapping(value = "/deleteDict/{dictId}")
    @ApiOperation(value = "删除字典")
    public Wrapper deleteDict(@PathVariable("dictId") Long dictId) {
        dictRpcApi.deleteDictById(dictId);
        return WrapMapper.ok();
    }

    @PostMapping(value = "/updateDict")
    @ApiOperation(value = "更新字典")
    public Wrapper updateDict(@RequestBody DictInfo dictInfo) {
        dictRpcApi.updateDict(dictInfo);
        return WrapMapper.ok();
    }

    @PostMapping(value = "/getDict/{dictId}")
    @ApiOperation(value = "获取字典")
    public Wrapper<DictInfo> getDict(@PathVariable("dictId") Long dictId) {
        return WrapMapper.ok(dictRpcApi.getDictById(dictId));
    }

    @PostMapping(value = "/listDict")
    @ApiOperation(value = "列表字典")
    public Wrapper<List<DictInfo>> listDict(@RequestBody(required = false) QueryDict queryDict) {
        return WrapMapper.ok(dictRpcApi.listDict(queryDict));
    }

    @PostMapping(value = "/queryDictsPage")
    @ApiOperation(value = "查询字典")
    public Wrapper<BaseDTO<DictInfo>> queryDictsPage(@RequestBody QueryDict queryDict) {
        return WrapMapper.ok(dictRpcApi.listDictPage(queryDict));
    }

    @PostMapping(value = "/getListDictItem/{dictCode}")
    @ApiOperation(value = "获取字典详情")
    public Wrapper<List<DictItemLabelValue>> getListDictItem(@PathVariable("dictCode") String dictCode) {
        return WrapMapper.ok(dictRpcApi.getListDictItem(dictCode));
    }

}
