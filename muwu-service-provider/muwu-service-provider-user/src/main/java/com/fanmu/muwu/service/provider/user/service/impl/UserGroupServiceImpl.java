/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanmu.muwu.common.base.enums.ErrorCodeEnum;
import com.fanmu.muwu.common.base.enums.GlobalStatusEnum;
import com.fanmu.muwu.common.base.pojo.BaseDTO;
import com.fanmu.muwu.common.coer.extension.service.impl.ServiceImpl;
import com.fanmu.muwu.common.coer.util.MyBatisUtil;
import com.fanmu.muwu.service.provider.user.api.exceptions.UserBizException;
import com.fanmu.muwu.service.provider.user.api.model.dto.usergroup.UserGroupInfo;
import com.fanmu.muwu.service.provider.user.api.model.query.usergroup.QueryUserGroup;
import com.fanmu.muwu.service.provider.user.api.service.UserGroupRpcApi;
import com.fanmu.muwu.service.provider.user.mapper.UserGroupMapper;
import com.fanmu.muwu.service.provider.user.model.domain.UserGroup;
import com.fanmu.muwu.service.provider.user.service.UserGroupService;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;

/**
 * <p>
 * 用户组 服务实现类
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
@DubboService(interfaceClass = UserGroupRpcApi.class)
public class UserGroupServiceImpl extends ServiceImpl<UserGroupMapper, UserGroup> implements UserGroupService {

    @Override
    public void insertUserGroup(UserGroupInfo userGroupInfo) {
        UserGroup userGroup = new UserGroup();
        BeanUtil.copyProperties(userGroupInfo, userGroup);
        super.save(userGroup);
    }

    @Override
    public void deleteUserGroupById(Long id) {
        Preconditions.checkArgument(id != null, ErrorCodeEnum.GL10000001.msg());

        super.removeById(id);
    }

    @Override
    public void updateUserGroup(UserGroupInfo userGroupInfo) {
        Preconditions.checkArgument(userGroupInfo.getId() != null, ErrorCodeEnum.GL10000001.msg());
        UserGroup userGroup = super.getById(userGroupInfo.getId());
        if (userGroup == null) {
            throw new UserBizException(ErrorCodeEnum.GL10000002, userGroup.getId());
        }
        // 修改数据
        userGroup = new UserGroup();
        BeanUtil.copyProperties(userGroupInfo, userGroup);
        super.updateById(userGroup);
    }

    @Override
    public UserGroupInfo getUserGroupById(Long id) {
        UserGroup userGroup = super.getById(id);
        if (userGroup == null) {
            throw new UserBizException(ErrorCodeEnum.GL10000002, id);
        }

        UserGroupInfo userGroupInfo = new UserGroupInfo();
        BeanUtil.copyProperties(userGroup, userGroupInfo);
        return userGroupInfo;
    }

    @Override
    public List<UserGroupInfo> listUserGroup() {
        return listUserGroup(null);
    }

    @Override
    public List<UserGroupInfo> listUserGroup(QueryUserGroup queryUserGroup) {
        LambdaQueryWrapper<UserGroup> userGroupQueryWrapper = new LambdaQueryWrapper<>();
        userGroupQueryWrapper.eq(UserGroup::getStatus, GlobalStatusEnum.ENABLE.getStatus());
        if (queryUserGroup != null) {
            if (StringUtils.isNotEmpty(queryUserGroup.getKeyword())) {
                userGroupQueryWrapper.like(UserGroup::getGroupName, queryUserGroup.getKeyword())
                        .or().like(UserGroup::getGroupCode, queryUserGroup.getKeyword());
            }
        }
        List<UserGroup> userGroups = super.list(userGroupQueryWrapper);
        List<UserGroupInfo> userGroupInfos = Lists.newArrayList();
        userGroups.forEach(userGroup -> {
            UserGroupInfo userGroupInfo = new UserGroupInfo();
            BeanUtil.copyProperties(userGroup, userGroupInfo);
            userGroupInfos.add(userGroupInfo);
        });
        return userGroupInfos;
    }

    @Override
    public BaseDTO<UserGroupInfo> listUserGroupPage(QueryUserGroup queryUserGroup) {
        Page page = MyBatisUtil.page(queryUserGroup);
        LambdaQueryWrapper<UserGroup> userGroupQueryWrapper = new LambdaQueryWrapper<>();
        userGroupQueryWrapper.eq(UserGroup::getStatus, GlobalStatusEnum.ENABLE.getStatus());
        if (StringUtils.isNotEmpty(queryUserGroup.getKeyword())) {
            userGroupQueryWrapper.like(UserGroup::getGroupName, queryUserGroup.getKeyword())
                    .or().like(UserGroup::getGroupCode, queryUserGroup.getKeyword());
        }
        IPage<UserGroup> userGroupIPage = super.page(page, userGroupQueryWrapper);

        BaseDTO<UserGroupInfo> baseDTO = MyBatisUtil.returnPage(userGroupIPage);
        List<UserGroupInfo> userGroupInfos = Lists.newArrayList();
        baseDTO.setData(userGroupInfos);
        userGroupIPage.getRecords().forEach(userGroup -> {
            UserGroupInfo userGroupInfo = new UserGroupInfo();
            BeanUtil.copyProperties(userGroup, userGroupInfo);
            userGroupInfos.add(userGroupInfo);
        });
        return baseDTO;
    }

}
