/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.config;

import com.fanmu.muwu.auth.security.authentication.json.JsonCodeAuthenticationSecurityConfig;
import com.fanmu.muwu.auth.security.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.fanmu.muwu.auth.security.authentication.social.SocialCodeAuthenticationSecurityConfig;
import com.fanmu.muwu.auth.security.authentication.wechat.WechatCodeAuthenticationSecurityConfig;
import com.fanmu.muwu.auth.security.code.ValidateCodeSecurityConfig;
import com.fanmu.muwu.common.config.properties.MuWuProperties;
import com.fanmu.muwu.common.security.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
public class WebSecurityServerConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PersistentTokenRepository persistentTokenRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    private final ValidateCodeSecurityConfig validateCodeSecurityConfig;

    private final SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    private final WechatCodeAuthenticationSecurityConfig wechatCodeAuthenticationSecurityConfig;

    private final JsonCodeAuthenticationSecurityConfig jsonCodeAuthenticationSecurityConfig;

    private final SocialCodeAuthenticationSecurityConfig socialCodeAuthenticationSecurityConfig;

    private final Oauth2AuthenticationConfig oauth2AuthenticationConfig;

    private final FormAuthenticationConfig formAuthenticationConfig;

    private final MuWuProperties muWuProperties;

    @Autowired
    public WebSecurityServerConfig(ValidateCodeSecurityConfig validateCodeSecurityConfig,
                                   SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig,
                                   WechatCodeAuthenticationSecurityConfig wechatCodeAuthenticationSecurityConfig,
                                   JsonCodeAuthenticationSecurityConfig jsonCodeAuthenticationSecurityConfig,
                                   SocialCodeAuthenticationSecurityConfig socialCodeAuthenticationSecurityConfig,
                                   Oauth2AuthenticationConfig oauth2AuthenticationConfig,
                                   FormAuthenticationConfig formAuthenticationConfig,
                                   MuWuProperties muWuProperties) {
        this.validateCodeSecurityConfig = validateCodeSecurityConfig;
        this.smsCodeAuthenticationSecurityConfig = smsCodeAuthenticationSecurityConfig;
        this.wechatCodeAuthenticationSecurityConfig = wechatCodeAuthenticationSecurityConfig;
        this.jsonCodeAuthenticationSecurityConfig = jsonCodeAuthenticationSecurityConfig;
        this.socialCodeAuthenticationSecurityConfig = socialCodeAuthenticationSecurityConfig;
        this.oauth2AuthenticationConfig = oauth2AuthenticationConfig;
        this.formAuthenticationConfig = formAuthenticationConfig;
        this.muWuProperties = muWuProperties;
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    /**
     * 默认密码处理器
     *
     * @return 密码加密器
     */
    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configure.
     *
     * @param http the http
     * @throws Exception the exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        oauth2AuthenticationConfig.configure(http);
        formAuthenticationConfig.configure(http);
        // 禁用session管理认证
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 添加记住我功能
        http.rememberMe().tokenRepository(persistentTokenRepository)
                // 有效期为两周
                .tokenValiditySeconds(3600 * 24 * 14)
                .rememberMeParameter(SecurityConstants.REMEMBER_ME_NAME)
                .rememberMeCookieName(SecurityConstants.REMEMBER_ME_NAME)
                .userDetailsService(userDetailsService);

        http.apply(validateCodeSecurityConfig)
                .and()
                .apply(smsCodeAuthenticationSecurityConfig)
                .and()
                .apply(wechatCodeAuthenticationSecurityConfig)
                .and()
                .apply(jsonCodeAuthenticationSecurityConfig)
                .and()
                .apply(socialCodeAuthenticationSecurityConfig);

        // 忽略URL
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();
        muWuProperties.getSecurity().getOauth2().getIgnore().getUrls().forEach(url -> registry.antMatchers(url).permitAll());

        // 其余所有请求全部需要鉴权认证
        registry.anyRequest().authenticated()
                // 我们这里不需要csrf、关闭跨站请求防护
                .and().csrf().disable();

        http.headers().frameOptions().disable();
    }
}
