/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.api.service;

import com.fanmu.muwu.common.base.pojo.BaseDTO;
import com.fanmu.muwu.service.provider.user.api.model.dto.user.AuthUser;
import com.fanmu.muwu.service.provider.user.api.model.dto.user.UserInfo;
import com.fanmu.muwu.service.provider.user.api.model.query.user.QueryUser;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户信息 rpc服务类
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
public interface UserRpcApi {

    void insertUser(UserInfo userInfo);

    void deleteUserById(Long id);

    void updateUser(UserInfo userInfo);

    UserInfo getUserById(Long id);

    List<UserInfo> listUser();

    List<UserInfo> listUser(QueryUser queryUser);

    BaseDTO<UserInfo> listUserPage(QueryUser queryUser);

    AuthUser getAuthUser(String username);

    List<Map<String, Object>> listUserTreeById(Long id);

    void saveUserRole(UserInfo userInfo);

    void resetUsertPassword(UserInfo userInfo);

}
