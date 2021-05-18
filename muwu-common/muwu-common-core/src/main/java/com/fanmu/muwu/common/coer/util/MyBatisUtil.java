/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.common.coer.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanmu.muwu.common.base.constant.GlobalConstant;
import com.fanmu.muwu.common.base.pojo.BaseDTO;
import com.fanmu.muwu.common.base.pojo.BaseQuery;
import org.apache.commons.lang3.StringUtils;

public class MyBatisUtil {

    public static Page page(BaseQuery baseQuery) {
        Page page = new Page();
        page.setCurrent(baseQuery.getPageNum());
        page.setSize(baseQuery.getPageSize());
        if (StringUtils.isNotEmpty(baseQuery.getOrderBy())) {
            String[] orders = baseQuery.getOrderBy().split(GlobalConstant.Symbol.COMMA);
            for (String order : orders) {
                String substring = order.substring(0, order.indexOf(GlobalConstant.Symbol.SPACE));
                if (order.contains("asc")) {
                    page.addOrder(OrderItem.asc(substring));
                } else {
                    page.addOrder(OrderItem.desc(substring));
                }
            }
        }
        return page;
    }

    public static BaseDTO returnPage(IPage iPage) {
        BaseDTO baseDTO = new BaseDTO();
        baseDTO.setPageNum(iPage.getCurrent());
        baseDTO.setPageSize(iPage.getSize());
        baseDTO.setTotal(iPage.getTotal());
        baseDTO.setPages(iPage.getPages());
        return baseDTO;
    }
}
