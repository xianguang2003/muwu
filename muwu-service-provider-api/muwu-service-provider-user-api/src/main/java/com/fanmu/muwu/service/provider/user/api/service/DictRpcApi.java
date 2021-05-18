/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.api.service;

import com.fanmu.muwu.common.base.pojo.BaseDTO;
import com.fanmu.muwu.service.provider.user.api.model.dto.dict.DictInfo;
import com.fanmu.muwu.service.provider.user.api.model.dto.dictitem.DictItemLabelValue;
import com.fanmu.muwu.service.provider.user.api.model.query.dict.QueryDict;

import java.util.List;
/**
 * <p>
 * 字典 rpc服务类
 * </p>
 *
 * @author mumu
 * @since 2020-10-25
 */
public interface DictRpcApi {

    void insertDict(DictInfo dictInfo);

    void deleteDictById(Long id);

    void updateDict(DictInfo dictInfo);

    DictInfo getDictById(Long id);

    List<DictInfo> listDict();

    List<DictInfo> listDict(QueryDict queryDict);

    BaseDTO<DictInfo> listDictPage(QueryDict queryDict);

    List<DictItemLabelValue> getListDictItem(String dictCode);

}
