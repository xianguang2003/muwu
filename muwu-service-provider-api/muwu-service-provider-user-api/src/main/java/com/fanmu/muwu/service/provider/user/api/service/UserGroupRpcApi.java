/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.api.service;

import com.fanmu.muwu.common.base.pojo.BaseDTO;
import com.fanmu.muwu.service.provider.user.api.model.dto.usergroup.UserGroupInfo;
import com.fanmu.muwu.service.provider.user.api.model.query.usergroup.QueryUserGroup;

import java.util.List;
/**
 * <p>
 * 用户组 rpc服务类
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
public interface UserGroupRpcApi {

    void insertUserGroup(UserGroupInfo userGroupInfo);

    void deleteUserGroupById(Long id);

    void updateUserGroup(UserGroupInfo userGroupInfo);

    UserGroupInfo getUserGroupById(Long id);

    List<UserGroupInfo> listUserGroup();

    List<UserGroupInfo> listUserGroup(QueryUserGroup queryUserGroup);

    BaseDTO<UserGroupInfo> listUserGroupPage(QueryUserGroup queryUserGroup);

}
