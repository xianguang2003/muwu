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
import com.fanmu.muwu.common.base.enums.ErrorCodeEnum;
import com.fanmu.muwu.common.base.enums.GlobalStatusEnum;
import com.fanmu.muwu.common.base.pojo.BaseDTO;
import com.fanmu.muwu.common.coer.extension.service.impl.ServiceImpl;
import com.fanmu.muwu.common.coer.util.MyBatisUtil;
import com.fanmu.muwu.service.provider.user.api.exceptions.UserBizException;
import com.fanmu.muwu.service.provider.user.api.model.dto.dictitem.DictItemInfo;
import com.fanmu.muwu.service.provider.user.api.model.query.dictitem.QueryDictItem;
import com.fanmu.muwu.service.provider.user.api.service.DictItemRpcApi;
import com.fanmu.muwu.service.provider.user.mapper.DictItemMapper;
import com.fanmu.muwu.service.provider.user.model.domain.DictItem;
import com.fanmu.muwu.service.provider.user.service.DictItemService;
import com.fanmu.muwu.service.provider.user.service.DictService;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>
 * 字典项表 服务实现类
 * </p>
 *
 * @author mumu
 * @since 2020-10-25
 */
@DubboService(interfaceClass = DictItemRpcApi.class)
public class DictItemServiceImpl extends ServiceImpl<DictItemMapper, DictItem> implements DictItemService {

    @Autowired
    DictService dictService;

    @Override
    public void insertDictItem(DictItemInfo dictItemInfo) {
        DictItem dictItem = new DictItem();
        BeanUtil.copyProperties(dictItemInfo, dictItem);
        super.save(dictItem);
        // 刷新缓存
        dictService.refreshCache(dictItem.getDictId());
    }

    @Override
    public void deleteDictItemById(Long id) {
        Preconditions.checkArgument(id != null, ErrorCodeEnum.GL10000001.msg());
        DictItem dictItem = super.getById(id);
        if (dictItem != null) {
            // 刷新缓存
            dictService.refreshCache(dictItem.getDictId());
        }
        super.removeById(id);
    }

    @Override
    public void updateDictItem(DictItemInfo dictItemInfo) {
        Preconditions.checkArgument(dictItemInfo.getId() != null, ErrorCodeEnum.GL10000001.msg());
        DictItem dictItem = super.getById(dictItemInfo.getId());
        if (dictItem == null) {
            throw new UserBizException(ErrorCodeEnum.GL10000002, dictItem.getId());
        }
        // 修改数据
        dictItem = new DictItem();
        BeanUtil.copyProperties(dictItemInfo, dictItem);
        super.updateById(dictItem);
        // 刷新缓存
        dictService.refreshCache(dictItem.getDictId());
    }

    @Override
    public DictItemInfo getDictItemById(Long id) {
        DictItem dictItem = super.getById(id);
        if (dictItem == null) {
            throw new UserBizException(ErrorCodeEnum.GL10000002, id);
        }

        DictItemInfo dictItemInfo = new DictItemInfo();
        BeanUtil.copyProperties(dictItem, dictItemInfo);
        return dictItemInfo;
    }

    @Override
    public List<DictItemInfo> listDictItem() {
        return listDictItem(null);
    }

    @Override
    public List<DictItemInfo> listDictItem(QueryDictItem queryDictItem) {
        LambdaQueryWrapper<DictItem> dictItemQueryWrapper = new LambdaQueryWrapper();
        dictItemQueryWrapper.eq(DictItem::getStatus, GlobalStatusEnum.ENABLE.getStatus());
        if (queryDictItem != null) {
            if (queryDictItem.getDictId() != null) {
                dictItemQueryWrapper.eq(DictItem::getDictId, queryDictItem.getDictId());
            }
            if (StringUtils.isNotEmpty(queryDictItem.getKeyword())) {
                dictItemQueryWrapper.like(DictItem::getItemText, queryDictItem.getKeyword());
            }
        }
        List<DictItemInfo> dictItemInfos = Lists.newArrayList();
        List<DictItem> dictItems = super.list(dictItemQueryWrapper);
        dictItems.forEach(dictItem -> {
            DictItemInfo dictItemInfo = new DictItemInfo();
            BeanUtil.copyProperties(dictItem, dictItemInfo);
            dictItemInfos.add(dictItemInfo);
        });
        return dictItemInfos;
    }

    @Override
    public BaseDTO<DictItemInfo> listDictItemPage(QueryDictItem queryDictItem) {
        Page page = MyBatisUtil.page(queryDictItem);
        LambdaQueryWrapper<DictItem> dictItemQueryWrapper = new LambdaQueryWrapper();
        dictItemQueryWrapper.eq(DictItem::getStatus, GlobalStatusEnum.ENABLE.getStatus())
                .eq(DictItem::getDictId, queryDictItem.getDictId());
        if (StringUtils.isNotEmpty(queryDictItem.getKeyword())) {
            dictItemQueryWrapper.like(DictItem::getItemText, queryDictItem.getKeyword());
        }
        IPage<DictItem> dictItemIPage = super.page(page, dictItemQueryWrapper);

        BaseDTO<DictItemInfo> baseDTO = MyBatisUtil.returnPage(dictItemIPage);
        List<DictItemInfo> dictItemInfos = Lists.newArrayList();
        baseDTO.setData(dictItemInfos);
        dictItemIPage.getRecords().forEach(dictItem -> {
            DictItemInfo dictItemInfo = new DictItemInfo();
            BeanUtil.copyProperties(dictItem, dictItemInfo);
            dictItemInfos.add(dictItemInfo);
        });
        return baseDTO;
    }

}
