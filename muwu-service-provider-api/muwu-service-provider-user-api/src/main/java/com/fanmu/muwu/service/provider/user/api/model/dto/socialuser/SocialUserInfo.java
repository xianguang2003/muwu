/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.api.model.dto.socialuser;

import com.fanmu.muwu.service.provider.user.api.model.dto.user.UserInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 社交用户信息
 * </p>
 *
 * @author mumu
 * @since 2020-08-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SocialUserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 认证端ID
     */
    private String clientId;

    /**
     * 用户标识
     */
    private String openId;

    /**
     * 用户全局标识
     */
    private String unionId;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String imageUrl;

    /**
     * 原始数据
     */
    private String originalData;

    private UserInfo userInfo;
}
