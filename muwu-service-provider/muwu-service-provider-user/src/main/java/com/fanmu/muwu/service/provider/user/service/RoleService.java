/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.service;

import com.fanmu.muwu.common.coer.extension.service.IService;
import com.fanmu.muwu.service.provider.user.api.service.RoleRpcApi;
import com.fanmu.muwu.service.provider.user.model.domain.Role;

/**
 * <p>
 * 角色信息 服务类
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
public interface RoleService extends IService<Role>, RoleRpcApi {

    /**
     * 刷新redis缓存，提供给网关使用
     */
    void refreshCache(String roleCode);

}
