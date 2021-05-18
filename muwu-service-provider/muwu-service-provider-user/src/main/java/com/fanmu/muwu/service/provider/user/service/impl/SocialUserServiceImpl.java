/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanmu.muwu.common.base.enums.ErrorCodeEnum;
import com.fanmu.muwu.common.base.pojo.BaseDTO;
import com.fanmu.muwu.common.coer.extension.service.impl.ServiceImpl;
import com.fanmu.muwu.common.coer.util.MyBatisUtil;
import com.fanmu.muwu.service.provider.user.api.exceptions.UserBizException;
import com.fanmu.muwu.service.provider.user.api.model.dto.socialuser.SocialUserInfo;
import com.fanmu.muwu.service.provider.user.api.model.dto.user.UserInfo;
import com.fanmu.muwu.service.provider.user.api.model.query.socialuser.QuerySocialUser;
import com.fanmu.muwu.service.provider.user.api.service.SocialUserRpcApi;
import com.fanmu.muwu.service.provider.user.mapper.SocialUserMapper;
import com.fanmu.muwu.service.provider.user.model.domain.SocialUser;
import com.fanmu.muwu.service.provider.user.service.SocialUserService;
import com.fanmu.muwu.service.provider.user.service.UserService;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>
 * 社交用户信息 服务实现类
 * </p>
 *
 * @author mumu
 * @since 2020-08-11
 */
@DubboService(interfaceClass = SocialUserRpcApi.class)
public class SocialUserServiceImpl extends ServiceImpl<SocialUserMapper, SocialUser> implements SocialUserService {

    @Autowired
    UserService userService;

    @Override
    public void insertSocialUser(SocialUserInfo socialUserInfo) {
        SocialUser socialUser = super.getOne(new QueryWrapper<SocialUser>().lambda()
                .eq(SocialUser::getClientId, socialUserInfo.getClientId())
                .eq(SocialUser::getOpenId, socialUserInfo.getOpenId()));
        if (socialUser != null) {
            throw new UserBizException(ErrorCodeEnum.GL10000003, socialUser.getId());
        }
        if (socialUserInfo.getUserInfo() != null) {
            userService.insertUser(socialUserInfo.getUserInfo());
        }
        socialUser = new SocialUser();
        BeanUtil.copyProperties(socialUserInfo, socialUser);
        socialUser.setUserId(socialUserInfo.getUserInfo().getId());
        super.save(socialUser);
    }

    @Override
    public void deleteSocialUserById(Long id) {
        Preconditions.checkArgument(id != null, ErrorCodeEnum.GL10000001.msg());

        super.removeById(id);
    }

    @Override
    public void updateSocialUser(SocialUserInfo socialUserInfo) {
        Preconditions.checkArgument(socialUserInfo.getId() != null, ErrorCodeEnum.GL10000001.msg());
        SocialUser socialUser = super.getById(socialUserInfo.getId());
        if (socialUser == null) {
            throw new UserBizException(ErrorCodeEnum.GL10000002, socialUser.getId());
        }
        // 修改数据
        socialUser = new SocialUser();
        BeanUtil.copyProperties(socialUserInfo, socialUser);
        super.updateById(socialUser);
    }

    @Override
    public SocialUserInfo getSocialUserById(Long id) {
        SocialUser socialUser = super.getById(id);
        if (socialUser == null) {
            throw new UserBizException(ErrorCodeEnum.GL10000002, id);
        }

        SocialUserInfo socialUserInfo = new SocialUserInfo();
        BeanUtil.copyProperties(socialUser, socialUserInfo);
        return socialUserInfo;
    }

    @Override
    public List<SocialUserInfo> listSocialUser() {
        List<SocialUser> socialUsers = super.list();
        List<SocialUserInfo> socialUserInfos = Lists.newArrayList();
        socialUsers.forEach(socialUser -> {
            SocialUserInfo socialUserInfo = new SocialUserInfo();
            BeanUtil.copyProperties(socialUser, socialUserInfo);
            socialUserInfos.add(socialUserInfo);
        });
        return socialUserInfos;
    }

    @Override
    public BaseDTO<SocialUserInfo> listSocialUserPage(QuerySocialUser querySocialUser) {
        Page page = MyBatisUtil.page(querySocialUser);
        QueryWrapper<SocialUser> socialUserQueryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(querySocialUser.getKeyword())) {
            socialUserQueryWrapper.lambda().like(SocialUser::getNickname, querySocialUser.getKeyword());
        }
        IPage<SocialUser> socialUserIPage = super.page(page, socialUserQueryWrapper);

        BaseDTO<SocialUserInfo> baseDTO = MyBatisUtil.returnPage(socialUserIPage);
        List<SocialUserInfo> socialUserInfos = Lists.newArrayList();
        baseDTO.setData(socialUserInfos);
        socialUserIPage.getRecords().forEach(socialUser -> {
            SocialUserInfo socialUserInfo = new SocialUserInfo();
            BeanUtil.copyProperties(socialUser, socialUserInfo);
            socialUserInfos.add(socialUserInfo);
        });
        return baseDTO;
    }

    @Override
    public SocialUserInfo getSocialUserByClientIdAndOpenId(String clientId, String openId) {
        SocialUser socialUser = super.getOne(new QueryWrapper<SocialUser>().lambda().eq(SocialUser::getClientId, clientId).eq(SocialUser::getOpenId, openId));
        SocialUserInfo socialUserInfo = new SocialUserInfo();
        BeanUtil.copyProperties(socialUser, socialUserInfo);
        UserInfo userInfo = userService.getUserById(socialUser.getUserId());
        socialUserInfo.setUserInfo(userInfo);
        return socialUserInfo;
    }
}
