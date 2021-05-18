/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanmu.muwu.common.base.constant.RedisConstant;
import com.fanmu.muwu.common.base.enums.ErrorCodeEnum;
import com.fanmu.muwu.common.base.enums.GlobalStatusEnum;
import com.fanmu.muwu.common.base.pojo.BaseDTO;
import com.fanmu.muwu.common.coer.extension.service.impl.ServiceImpl;
import com.fanmu.muwu.common.coer.util.MyBatisUtil;
import com.fanmu.muwu.service.provider.user.api.exceptions.UserBizException;
import com.fanmu.muwu.service.provider.user.api.model.dto.dict.DictInfo;
import com.fanmu.muwu.service.provider.user.api.model.dto.dictitem.DictItemInfo;
import com.fanmu.muwu.service.provider.user.api.model.dto.dictitem.DictItemLabelValue;
import com.fanmu.muwu.service.provider.user.api.model.query.dict.QueryDict;
import com.fanmu.muwu.service.provider.user.api.model.query.dictitem.QueryDictItem;
import com.fanmu.muwu.service.provider.user.api.service.DictRpcApi;
import com.fanmu.muwu.service.provider.user.mapper.DictMapper;
import com.fanmu.muwu.service.provider.user.model.domain.Dict;
import com.fanmu.muwu.service.provider.user.service.DictItemService;
import com.fanmu.muwu.service.provider.user.service.DictService;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * <p>
 * 字典 服务实现类
 * </p>
 *
 * @author mumu
 * @since 2020-10-25
 */
@DubboService(interfaceClass = DictRpcApi.class)
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Autowired
    DictItemService dictItemService;

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public void insertDict(DictInfo dictInfo) {
        Dict dict = super.getOne(new LambdaQueryWrapper<Dict>().eq(Dict::getDictCode, dictInfo.getDictCode()));
        if (dict != null) {
            throw new UserBizException(ErrorCodeEnum.GL10000003, dict.getId());
        }
        dict = new Dict();
        BeanUtil.copyProperties(dictInfo, dict);
        super.save(dict);
    }

    @Override
    public void deleteDictById(Long id) {
        Preconditions.checkArgument(id != null, ErrorCodeEnum.GL10000001.msg());
        // 刷新缓存
        refreshCache(id);
        super.removeById(id);
    }

    @Override
    public void updateDict(DictInfo dictInfo) {
        Preconditions.checkArgument(dictInfo.getId() != null, ErrorCodeEnum.GL10000001.msg());
        Dict dict = super.getById(dictInfo.getId());
        if (dict == null) {
            throw new UserBizException(ErrorCodeEnum.GL10000002, dict.getId());
        }
        // 修改数据
        dict = new Dict();
        BeanUtil.copyProperties(dictInfo, dict);
        super.updateById(dict);
        // 刷新缓存
        refreshCache(dict.getId());
    }

    @Override
    public DictInfo getDictById(Long id) {
        Dict dict = super.getById(id);
        if (dict == null) {
            throw new UserBizException(ErrorCodeEnum.GL10000002, id);
        }

        DictInfo dictInfo = new DictInfo();
        BeanUtil.copyProperties(dict, dictInfo);
        return dictInfo;
    }

    @Override
    public List<DictInfo> listDict() {
        return listDict(null);
    }

    @Override
    public List<DictInfo> listDict(QueryDict queryDict) {
        LambdaQueryWrapper<Dict> dictQueryWrapper = new LambdaQueryWrapper<>();
        dictQueryWrapper.eq(Dict::getStatus, GlobalStatusEnum.ENABLE.getStatus());
        if (queryDict != null) {
            if (StringUtils.isNotEmpty(queryDict.getKeyword())) {
                dictQueryWrapper.like(Dict::getDictName, queryDict.getKeyword())
                        .or().like(Dict::getDictCode, queryDict.getKeyword());
            }
        }
        List<Dict> dicts = super.list(dictQueryWrapper);
        List<DictInfo> dictInfos = Lists.newArrayList();
        dicts.forEach(dict -> {
            DictInfo dictInfo = new DictInfo();
            BeanUtil.copyProperties(dict, dictInfo);
            dictInfos.add(dictInfo);
        });
        return dictInfos;
    }

    @Override
    public BaseDTO<DictInfo> listDictPage(QueryDict queryDict) {
        Page page = MyBatisUtil.page(queryDict);
        LambdaQueryWrapper<Dict> dictQueryWrapper = new LambdaQueryWrapper<>();
        dictQueryWrapper.eq(Dict::getStatus, GlobalStatusEnum.ENABLE.getStatus());
        if (StringUtils.isNotEmpty(queryDict.getKeyword())) {
            dictQueryWrapper.like(Dict::getDictName, queryDict.getKeyword())
                    .or().like(Dict::getDictCode, queryDict.getKeyword());
        }
        IPage<Dict> dictIPage = super.page(page, dictQueryWrapper);

        BaseDTO<DictInfo> baseDTO = MyBatisUtil.returnPage(dictIPage);
        List<DictInfo> dictInfos = Lists.newArrayList();
        baseDTO.setData(dictInfos);
        dictIPage.getRecords().forEach(dict -> {
            DictInfo dictInfo = new DictInfo();
            BeanUtil.copyProperties(dict, dictInfo);
            dictInfos.add(dictInfo);
        });
        return baseDTO;
    }

    @Override
    public void refreshCache(Long id) {
        Dict dict = super.getById(id);
        if (dict == null) {
            return;
        }
        redisTemplate.opsForHash().delete(RedisConstant.DICT_ITEM_KEY, dict.getDictCode());
    }

    @Override
    public List<DictItemLabelValue> getListDictItem(String dictCode) {
        Object dictItemsCache = redisTemplate.opsForHash().get(RedisConstant.DICT_ITEM_KEY, dictCode);
        if (dictItemsCache != null) {
            return (List<DictItemLabelValue>) dictItemsCache;
        }
        Dict dict = super.getOne(new LambdaQueryWrapper<Dict>()
                .eq(Dict::getDictCode, dictCode)
                .eq(Dict::getStatus, GlobalStatusEnum.ENABLE));
        if (dict == null) {
            return null;
        }
        List<DictItemLabelValue> dictItemLabelValues = Lists.newArrayList();
        List<DictItemInfo> dictItemInfos = dictItemService.listDictItem(QueryDictItem.builder().dictId(dict.getId()).build());
        for (DictItemInfo dictItemInfo : dictItemInfos) {
            DictItemLabelValue dictItemLabelValue = new DictItemLabelValue();
            BeanUtil.copyProperties(dictItemInfo, dictItemLabelValue);
            dictItemLabelValues.add(dictItemLabelValue);
        }
        redisTemplate.opsForHash().put(RedisConstant.DICT_ITEM_KEY, dictCode, dictItemLabelValues);
        return dictItemLabelValues;
    }
}
