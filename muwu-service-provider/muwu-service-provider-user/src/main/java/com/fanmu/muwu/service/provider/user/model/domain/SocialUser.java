/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.model.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fanmu.muwu.common.coer.extension.model.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 社交用户信息
 * </p>
 *
 * @author mumu
 * @since 2020-08-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mw_social_user")
public class SocialUser extends BaseModel {

    private static final long serialVersionUID=1L;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 认证端ID
     */
    @TableField("client_id")
    private String clientId;

    /**
     * 用户标识
     */
    @TableField("open_id")
    private String openId;

    /**
     * 用户全局标识
     */
    @TableField("union_id")
    private String unionId;

    /**
     * 用户昵称
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 用户头像
     */
    @TableField("image_url")
    private String imageUrl;

    /**
     * 原始数据
     */
    @TableField("original_data")
    private String originalData;


}
