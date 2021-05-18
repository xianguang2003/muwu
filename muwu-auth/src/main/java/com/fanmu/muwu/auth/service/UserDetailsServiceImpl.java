/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.service;

import com.fanmu.muwu.auth.manager.UserManager;
import com.fanmu.muwu.auth.security.ExtensionUserDetailsService;
import com.fanmu.muwu.common.security.SecurityUser;
import com.fanmu.muwu.service.provider.user.api.model.dto.user.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserDetailsServiceImpl implements ExtensionUserDetailsService {

    @Autowired
    UserManager userManager;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        AuthUser authUser = userManager.getAuthUser(s);
        if (authUser == null) {
            throw new UsernameNotFoundException("账号或密码错误");
        }
        return createSpringSecurityUser(authUser);
    }

    @Override
    public SocialUserDetails loadUserByUserId(String s) throws UsernameNotFoundException {
        return null;
    }

    private UserDetails createSpringSecurityUser(AuthUser authUser) {
        List<GrantedAuthority> grantedAuthorities = authUser.getRoles().stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
        return new SecurityUser(authUser.getUserId(), authUser.getNickname(), authUser.getUsername(), authUser.getPassword(), grantedAuthorities);
    }

    @Override
    public UserDetails loadUserByWechatCode(String appId, String code) throws UsernameNotFoundException {
        return null;
    }

    /*@Autowired
    private MiniAppManager miniAppManager;
    @Autowired
    private WechatAppService wechatAppService;
    @Autowired
    private WechatUserService wechatUserService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
//        AuthUserDTO authUser = ucUserService.getAuthUser(s);
//        if (authUser == null) {
//            throw new UsernameNotFoundException("账号或密码错误");
//        }
//        return createSpringSecurityUser(authUser);
        List<GrantedAuthority> grantedAuthorities = Lists.newArrayList("12", "query").stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
        return new User("admin", "$2a$10$LvlRXYpsKRdqahrB/AxQmuQBKyfzD9svcYWd7WLi5aPTNYx3BzvKG", grantedAuthorities);
    }

    @Override
    public UserDetails loadUserByWechatCode(String appId, String code) throws UsernameNotFoundException {
        WechatApp wechatApp = wechatAppService.getOne(new LambdaQueryWrapper<WechatApp>().eq(WechatApp::getAppId, appId));
        if (wechatApp == null) {
            throw new UsernameNotFoundException("未找到微信配置");
        }
        WxMaService wxMaService = miniAppManager.getWxMaService(appId);
        if (wxMaService == null) {
            throw new UsernameNotFoundException("未找到微信配置");
        }
        try {
            WxMaJscode2SessionResult sessionInfo = wxMaService.getUserService().getSessionInfo(code);
            log.info("appId:{};sessionInfo:{}", appId, JSON.toJSON(sessionInfo));
            if (sessionInfo == null) {
                throw new UsernameNotFoundException("wechatApp error");
            }
            WechatUser wechatUser = wechatUserService.getOne(new LambdaQueryWrapper<WechatUser>().eq(WechatUser::getAppId, appId).eq(WechatUser::getOpenid, sessionInfo.getOpenid()));
            if (wechatUser == null) {
                wechatUser = new WechatUser();
                wechatUser.setAppId(appId);
                wechatUser.setAppWid(wechatApp.getWid());
                wechatUser.setOpenid(sessionInfo.getOpenid());
                wechatUser.setUnionid(sessionInfo.getUnionid());
                wechatUserService.save(wechatUser);
                wechatUser = wechatUserService.getOne(new LambdaQueryWrapper<WechatUser>().eq(WechatUser::getAppId, appId).eq(WechatUser::getOpenid, sessionInfo.getOpenid()));
            }
            // 防止sessionKey
            String name = wechatUser.getOpenid() + ":" + UUID.randomUUID().toString();
            return new WechatSecurityUser(name, appId, sessionInfo.getSessionKey(), wechatUser.getOpenid(), wechatUser.getNickname(), wechatUser.getSex(), null);
//            return new WechatSecurityUser(appId, "sessionInfo.getSessionKey()", code, "wechatUser.getNickname()", 1, null);
        } catch (WxErrorException e) {
            log.error("loadUserByWechatCode error ", e);
            throw new UsernameNotFoundException(e.getMessage());
        } catch (Exception e) {
            log.error("loadUserByWechatCode error ", e);
            throw new UsernameNotFoundException("wechatApp error");
        }
    }*/

}
