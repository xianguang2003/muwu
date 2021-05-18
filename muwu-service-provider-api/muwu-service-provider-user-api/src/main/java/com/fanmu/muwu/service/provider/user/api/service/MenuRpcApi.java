/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.api.service;

import com.fanmu.muwu.common.base.pojo.BaseDTO;
import com.fanmu.muwu.service.provider.user.api.model.dto.menu.MenuInfo;
import com.fanmu.muwu.service.provider.user.api.model.query.menu.QueryMenu;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单 rpc服务类
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
public interface MenuRpcApi {

    void insertMenu(MenuInfo menuInfo);

    void deleteMenuById(Long id);

    void updateMenu(MenuInfo menuInfo);

    MenuInfo getMenuById(Long id);

    List<MenuInfo> listMenu();

    List<MenuInfo> listMenu(QueryMenu queryMenu);

    BaseDTO<MenuInfo> listMenuPage(QueryMenu queryMenu);

    List<Map<String, Object>> listMenuTree();

    void sortMenu(List<MenuInfo> menuInfos);

    void updateMenuPermission(MenuInfo menuInfo);

}
