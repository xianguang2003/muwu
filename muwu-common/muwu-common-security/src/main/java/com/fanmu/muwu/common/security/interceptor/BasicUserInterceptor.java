/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.common.security.interceptor;

import com.fanmu.muwu.common.base.constant.GlobalConstant;
import com.fanmu.muwu.common.base.pojo.BasicUser;
import com.fanmu.muwu.common.security.SecurityUser;
import com.fanmu.muwu.common.security.SecurityUtils;
import com.fanmu.muwu.common.util.support.ThreadLocalMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class BasicUserInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        SecurityUser securityUser = SecurityUtils.getCurrentUser();
        if (securityUser != null) {
            BasicUser basicUser = new BasicUser();
            basicUser.setUserId(securityUser.getUserId());
            basicUser.setUsername(securityUser.getUsername());
            basicUser.setNickname(securityUser.getNickname());
            ThreadLocalMap.put(GlobalConstant.Sys.CURRENT_BASIC_USER, basicUser);
        }
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        ThreadLocalMap.remove(GlobalConstant.Sys.CURRENT_BASIC_USER);
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (ex != null) {
            ThreadLocalMap.remove(GlobalConstant.Sys.CURRENT_BASIC_USER);
        }
        super.afterCompletion(request, response, handler, ex);
    }
}
