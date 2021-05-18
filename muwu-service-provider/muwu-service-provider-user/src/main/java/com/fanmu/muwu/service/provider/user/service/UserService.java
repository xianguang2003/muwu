/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.service;

import com.fanmu.muwu.service.provider.user.api.service.UserRpcApi;
import com.fanmu.muwu.service.provider.user.model.domain.User;
import com.fanmu.muwu.common.coer.extension.service.IService;

/**
 * <p>
 * 用户信息 服务类
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
public interface UserService extends IService<User>, UserRpcApi {

}
