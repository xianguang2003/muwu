/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.api.service;

import com.fanmu.muwu.common.base.pojo.BaseDTO;
import com.fanmu.muwu.service.provider.user.api.model.dto.socialuser.SocialUserInfo;
import com.fanmu.muwu.service.provider.user.api.model.query.socialuser.QuerySocialUser;

import java.util.List;
/**
 * <p>
 * 社交用户信息 rpc服务类
 * </p>
 *
 * @author mumu
 * @since 2020-08-11
 */
public interface SocialUserRpcApi {

    void insertSocialUser(SocialUserInfo socialUserInfo);

    void deleteSocialUserById(Long id);

    void updateSocialUser(SocialUserInfo socialUserInfo);

    SocialUserInfo getSocialUserById(Long id);

    List<SocialUserInfo> listSocialUser();

    BaseDTO<SocialUserInfo> listSocialUserPage(QuerySocialUser querySocialUser);

    SocialUserInfo getSocialUserByClientIdAndOpenId(String clientId, String openId);

}
