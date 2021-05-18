/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.service;

import com.fanmu.muwu.service.provider.user.api.service.PermissionRpcApi;
import com.fanmu.muwu.service.provider.user.model.domain.Permission;
import com.fanmu.muwu.common.coer.extension.service.IService;

/**
 * <p>
 * 权限 服务类
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
public interface PermissionService extends IService<Permission>, PermissionRpcApi {

    /**
     * 刷新redis缓存，提供给网关使用
     */
    void refreshCache(Permission permission);

}
