/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.security.social;

import com.fanmu.muwu.common.security.SecurityConstants;
import com.fanmu.muwu.auth.security.social.support.MuWuSpringSocialConfigurer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;


/**
 * The class Spring social configurer post processor.
 *
 */
@Component
public class SpringSocialConfigurerPostProcessor implements BeanPostProcessor {

    /**
     * Post process before initialization object.
     *
     * @param bean     the bean
     * @param beanName the bean name
     * @return the object
     * @throws BeansException the beans exception
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * Post process after initialization object.
     *
     * @param bean     the bean
     * @param beanName the bean name
     * @return the object
     * @throws BeansException the beans exception
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        String muWuSocialSecurityConfig = "muWuSocialSecurityConfig";
        if (StringUtils.equals(beanName, muWuSocialSecurityConfig)) {
            MuWuSpringSocialConfigurer config = (MuWuSpringSocialConfigurer) bean;
            config.signupUrl(SecurityConstants.DEFAULT_SOCIAL_USER_INFO_URL);
            return config;
        }
        return bean;
    }

}
