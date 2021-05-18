/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.security.social;

import com.fanmu.muwu.auth.security.AppSecretException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.concurrent.TimeUnit;

/**
 * app环境下替换providerSignInUtils，避免由于没有session导致读不到社交用户信息的问题
 *
 */
@Component
public class AppSingUpUtils {

	private final RedisTemplate<String, Object> redisTemplate;

	private final UsersConnectionRepository usersConnectionRepository;

	private final ConnectionFactoryLocator connectionFactoryLocator;

	/**
	 * Instantiates a new App sing up utils.
	 *
	 * @param redisTemplate             the redis template
	 * @param usersConnectionRepository the users connection repository
	 * @param connectionFactoryLocator  the connection factory locator
	 */
	@Autowired
	public AppSingUpUtils(RedisTemplate<String, Object> redisTemplate, UsersConnectionRepository usersConnectionRepository, ConnectionFactoryLocator connectionFactoryLocator) {
		this.redisTemplate = redisTemplate;
		this.usersConnectionRepository = usersConnectionRepository;
		this.connectionFactoryLocator = connectionFactoryLocator;
	}

	/**
	 * 缓存社交网站用户信息到redis
	 *
	 * @param request        the request
	 * @param connectionData the connection data
	 */
	public void saveConnectionData(WebRequest request, ConnectionData connectionData) {
		redisTemplate.opsForValue().set(getKey(request), this.getMuWuConnectionData(connectionData), 10, TimeUnit.MINUTES);
	}

	/**
	 * 将缓存的社交网站用户信息与系统注册用户信息绑定
	 *
	 * @param request the request
	 * @param userId  the user id
	 */
	public void doPostSignUp(WebRequest request, String userId) {
		String key = getKey(request);
        if(!redisTemplate.hasKey(key)){
            throw new AppSecretException("无法找到缓存的用户社交账号信息");
        }
        MuWuConnectionData muWuConnectionData = (MuWuConnectionData) redisTemplate.opsForValue().get(key);
        ConnectionData connectionData = this.getConnectionData(muWuConnectionData);

        Connection<?> connection = connectionFactoryLocator.getConnectionFactory(connectionData.getProviderId()).createConnection(connectionData);
        usersConnectionRepository.createConnectionRepository(userId).addConnection(connection);
        redisTemplate.delete(key);

	}

	/**
	 * 获取redis key
	 */
	private String getKey(WebRequest request) {
		String deviceId = request.getHeader("deviceId");
		if (StringUtils.isBlank(deviceId)) {
			throw new AppSecretException("设备id参数不能为空");
		}
		return "muwu:security:social.connect." + deviceId;
	}

	private MuWuConnectionData getMuWuConnectionData(ConnectionData connectionData) {
		MuWuConnectionData muWuConnectionData = new MuWuConnectionData();
		BeanUtils.copyProperties(connectionData, muWuConnectionData);
		return muWuConnectionData;
	}

    private ConnectionData getConnectionData(MuWuConnectionData muWuConnectionData) {
		String providerId = muWuConnectionData.getProviderId();
		String providerUserId = muWuConnectionData.getProviderUserId();
		String displayName = muWuConnectionData.getDisplayName();
		String profileUrl = muWuConnectionData.getProfileUrl();
		String imageUrl = muWuConnectionData.getImageUrl();
		String accessToken = muWuConnectionData.getAccessToken();
		String secret = muWuConnectionData.getSecret();
		String refreshToken = muWuConnectionData.getRefreshToken();
		Long expireTime = muWuConnectionData.getExpireTime();
		return new ConnectionData(providerId, providerUserId, displayName, profileUrl, imageUrl, accessToken, secret, refreshToken, expireTime);
	}

}
