/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.security.social.qq.connet;

import com.fanmu.muwu.auth.security.social.qq.api.QQ;
import com.fanmu.muwu.auth.security.social.qq.api.QQUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * The class Qq adapter.
 *
 */
public class QQAdapter implements ApiAdapter<QQ> {

	/**
	 * Test boolean.
	 *
	 * @param api the api
	 *
	 * @return the boolean
	 */
	@Override
	public boolean test(QQ api) {
		return true;
	}

	/**
	 * Sets connection values.
	 *
	 * @param api    the api
	 * @param values the values
	 */
	@Override
	public void setConnectionValues(QQ api, ConnectionValues values) {
		QQUserInfo userInfo = api.getUserInfo();
		// TODO 如果用户存在异步更新用户信息
		values.setDisplayName(userInfo.getNickname());
		values.setImageUrl(userInfo.getFigureUrlQq1());
		values.setProfileUrl(null);
		values.setProviderUserId(userInfo.getOpenId());
	}

	/**
	 * Fetch user profile user profile.
	 *
	 * @param api the api
	 *
	 * @return the user profile
	 */
	@Override
	public UserProfile fetchUserProfile(QQ api) {
		return null;
	}

	/**
	 * Update status.
	 *
	 * @param api     the api
	 * @param message the message
	 */
	@Override
	public void updateStatus(QQ api, String message) {
		//do noting
	}

}
