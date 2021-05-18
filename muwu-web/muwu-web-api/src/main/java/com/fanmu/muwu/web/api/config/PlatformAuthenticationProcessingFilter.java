/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.web.api.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.net.URLDecoder;
import com.fanmu.muwu.common.security.SecurityUser;
import com.fanmu.muwu.common.web.extension.wrapper.WrapMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PlatformAuthenticationProcessingFilter implements Filter {

    private final static Log logger = LogFactory.getLog(PlatformAuthenticationProcessingFilter.class);

    final String BASIC_USER_INFO = "Basic-User-Info";

    private ObjectMapper objectMapper;

    public PlatformAuthenticationProcessingFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        String userInfo = URLDecoder.decode(request.getHeader(BASIC_USER_INFO), Charset.defaultCharset());
        try {
            if (StringUtils.isNotEmpty(userInfo)) {
                Map value = objectMapper.readValue(userInfo, Map.class);
                String username = Objects.isNull(value.get("username")) ? null : value.get("username").toString();
                String password = Objects.isNull(value.get("password")) ? "" : value.get("password").toString();
                Boolean enabled = Boolean.parseBoolean(Objects.isNull(value.get("enabled")) ? null : value.get("enabled").toString());
                Boolean accountNonExpired = Boolean.parseBoolean(Objects.isNull(value.get("accountNonExpired")) ? null : value.get("accountNonExpired").toString());
                Boolean credentialsNonExpired = Boolean.parseBoolean(Objects.isNull(value.get("credentialsNonExpired")) ? null : value.get("credentialsNonExpired").toString());
                Boolean accountNonLocked = Boolean.parseBoolean(Objects.isNull(value.get("accountNonLocked")) ? null : value.get("accountNonLocked").toString());
                Long userId = Objects.isNull(value.get("userId")) ? null : Long.parseLong(value.get("userId").toString());
                String nickname = Objects.isNull(value.get("nickname")) ? null : value.get("nickname").toString();

                List<Map> authorities = Objects.isNull(value.get("authorities")) ? null : (List<Map>) value.get("authorities");
                List<GrantedAuthority> authoritiesTmp = Lists.newArrayList();
                if (CollUtil.isNotEmpty(authorities)) {
                    for (Map authority : authorities) {
                        if (CollUtil.isNotEmpty(authority)) {
                            authoritiesTmp.add(new SimpleGrantedAuthority(authority.get("authority").toString()));
                        }
                    }
                }
                SecurityUser securityUser = new SecurityUser(username,
                        password,
                        enabled,
                        accountNonExpired,
                        credentialsNonExpired,
                        accountNonLocked,
                        authoritiesTmp,
                        userId,
                        nickname);
                if (securityUser != null) {
                    // 设置基础用户信息
                    SecurityContextHolder.getContext().setAuthentication(new PlatformAuthenticationToken(securityUser.getAuthorities(), securityUser));
                } else {
                    throw new PlatformAuthenticationException("用户信息序列化失败");
                }
            } else {
                throw new PlatformAuthenticationException("用户信息验证失败");
            }
        } catch (AuthenticationException failed) {
            logger.error("平台认证失败 " + failed.getMessage());
//            unsuccessfulAuthentication(request, response, failed);
        } catch (Exception failed) {
            logger.error("平台认证失败 " + failed.getMessage());
//            unsuccessfulAuthentication(request, response, null);
        }

        filterChain.doFilter(request, response);
    }

    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        SecurityContextHolder.clearContext();

        if (logger.isDebugEnabled()) {
            logger.debug("Updated SecurityContextHolder to contain null Authentication");
        }
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        if (failed != null) {
            response.getWriter().write(objectMapper.writeValueAsString(WrapMapper.error(failed.getMessage())));
        } else {
            response.getWriter().write(objectMapper.writeValueAsString(WrapMapper.error("平台认证失败")));
        }
    }
}
