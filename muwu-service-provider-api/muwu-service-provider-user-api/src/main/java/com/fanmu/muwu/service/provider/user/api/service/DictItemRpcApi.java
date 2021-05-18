/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.api.service;

import com.fanmu.muwu.common.base.pojo.BaseDTO;
import com.fanmu.muwu.service.provider.user.api.model.dto.dictitem.DictItemInfo;
import com.fanmu.muwu.service.provider.user.api.model.query.dictitem.QueryDictItem;

import java.util.List;
/**
 * <p>
 * 字典项表 rpc服务类
 * </p>
 *
 * @author mumu
 * @since 2020-10-25
 */
public interface DictItemRpcApi {

    void insertDictItem(DictItemInfo dictItemInfo);

    void deleteDictItemById(Long id);

    void updateDictItem(DictItemInfo dictItemInfo);

    DictItemInfo getDictItemById(Long id);

    List<DictItemInfo> listDictItem();

    List<DictItemInfo> listDictItem(QueryDictItem queryDictItem);

    BaseDTO<DictItemInfo> listDictItemPage(QueryDictItem queryDictItem);

}
