/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.service;

import com.fanmu.muwu.service.provider.user.model.domain.Dict;
import com.fanmu.muwu.common.coer.extension.service.IService;
import com.fanmu.muwu.service.provider.user.api.service.DictRpcApi;

/**
 * <p>
 * 字典 服务类
 * </p>
 *
 * @author mumu
 * @since 2020-10-25
 */
public interface DictService extends IService<Dict>, DictRpcApi {

    /**
     * 刷新redis缓存
     */
    void refreshCache(Long id);

}
