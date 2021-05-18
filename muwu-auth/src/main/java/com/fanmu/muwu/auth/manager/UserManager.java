/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.manager;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.fanmu.muwu.auth.model.dto.AuthLog;
import com.fanmu.muwu.common.security.SecurityUtils;
import com.fanmu.muwu.service.provider.user.api.model.dto.loginlog.LoginLogInfo;
import com.fanmu.muwu.service.provider.user.api.model.dto.socialuser.SocialUserInfo;
import com.fanmu.muwu.service.provider.user.api.model.dto.user.AuthUser;
import com.fanmu.muwu.service.provider.user.api.model.dto.user.UserInfo;
import com.fanmu.muwu.service.provider.user.api.service.LoginLogRpcApi;
import com.fanmu.muwu.service.provider.user.api.service.SocialUserRpcApi;
import com.fanmu.muwu.service.provider.user.api.service.UserRpcApi;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class UserManager {

    @DubboReference
    UserRpcApi userRpcApi;

    @DubboReference
    SocialUserRpcApi socialUserRpcApi;

    @DubboReference
    LoginLogRpcApi loginLogRpcApi;

    public AuthUser getAuthUser(String username) {
        return userRpcApi.getAuthUser(username);
    }

    public SocialUserInfo getSocialUserInfo(String clientId, String openId) {
        return socialUserRpcApi.getSocialUserByClientIdAndOpenId(clientId, openId);
    }

    public SocialUserInfo addSocialUser(String client_id, OAuth2User auth2User) {
        SocialUserInfo socialUserInfo = new SocialUserInfo();
        socialUserInfo.setClientId(client_id);
        if (client_id.equals("weixin")) {
            weiXinAttributesMapping(socialUserInfo, auth2User);
        }
        socialUserRpcApi.insertSocialUser(socialUserInfo);
        return socialUserInfo;
    }

    private void weiXinAttributesMapping(SocialUserInfo socialUserInfo, OAuth2User auth2User) {
        socialUserInfo.setOpenId(auth2User.getAttribute("openid"));
        socialUserInfo.setUnionId(auth2User.getAttribute("unionid"));
        socialUserInfo.setNickname(auth2User.getAttribute("nickname"));
        socialUserInfo.setImageUrl(auth2User.getAttribute("headimgurl"));
        UserInfo userInfo = new UserInfo();
        userInfo.setNickname(auth2User.getAttribute("nickname"));
        userInfo.setSex(auth2User.getAttribute("sex"));
        userInfo.setUsername(RandomUtil.randomString(10));
        userInfo.setPassword(SecurityUtils.passwordEncoder("123456"));
        socialUserInfo.setUserInfo(userInfo);
    }

    public void saveLoginLog(AuthLog authLog) {
        LoginLogInfo loginLogInfo = new LoginLogInfo();
        BeanUtil.copyProperties(authLog, loginLogInfo);
        loginLogRpcApi.save(loginLogInfo);
    }

}
