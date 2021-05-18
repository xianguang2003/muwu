/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.service;

import com.fanmu.muwu.common.coer.extension.service.IService;
import com.fanmu.muwu.service.provider.user.api.service.MenuRpcApi;
import com.fanmu.muwu.service.provider.user.model.domain.Menu;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单 服务类
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
public interface MenuService extends IService<Menu>, MenuRpcApi {

    List<Map<String, Object>> tree(List<Menu> menus, Long parentId);

}
