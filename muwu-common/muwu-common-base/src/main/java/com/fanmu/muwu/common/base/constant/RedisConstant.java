/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.common.base.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisConstant {

    public static final String ROLE_PERMISSION_URL_KEY = "muwu:role";

    public static final String DICT_ITEM_KEY = "muwu:dict";

    public static final String PERMISSION_WHITELIST_URLS_KEY = "muwu:permission:whitelist:urls";
}
