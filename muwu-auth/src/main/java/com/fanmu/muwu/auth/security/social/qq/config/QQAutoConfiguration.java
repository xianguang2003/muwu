/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.security.social.qq.config;

import com.fanmu.muwu.common.config.properties.MuWuProperties;
import com.fanmu.muwu.common.config.properties.QQProperties;
import com.fanmu.muwu.auth.security.social.qq.connet.QQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;


/**
 * The class Qq auto config.
 *
 */
@Configuration
@ConditionalOnProperty(prefix = "muwu.security.social.qq", name = "app-id")
public class QQAutoConfiguration extends SocialConfigurerAdapter {

    private final MuWuProperties muWuProperties;

    @Autowired
    public QQAutoConfiguration(MuWuProperties muWuProperties) {
        this.muWuProperties = muWuProperties;
    }

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer configurer, Environment environment) {
        configurer.addConnectionFactory(createConnectionFactory());
    }

    /**
     * Create connection factory connection factory.
     *
     * @return the connection factory
     */
    protected ConnectionFactory<?> createConnectionFactory() {
        QQProperties qqConfig = muWuProperties.getSecurity().getSocial().getQq();
        return new QQConnectionFactory(qqConfig.getProviderId(), qqConfig.getAppId(), qqConfig.getAppSecret());
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        return null;
    }
}
