/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.common.util;


import cn.hutool.core.date.DateField;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * The class Date util.
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {

    /**
     * 获取系统前时间.
     *
     * @param minute the minute
     * @return the before time[yyyy-MM-dd HH:mm:ss]
     */
    public static String getBeforeTime(int minute) {
        Date newDate = cn.hutool.core.date.DateUtil.offset(new Date(), DateField.MINUTE, -minute);
        return cn.hutool.core.date.DateUtil.formatDateTime(newDate);
    }
}
