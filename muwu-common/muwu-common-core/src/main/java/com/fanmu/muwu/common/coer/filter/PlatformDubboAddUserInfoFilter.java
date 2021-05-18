/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.common.coer.filter;

import com.fanmu.muwu.common.base.constant.GlobalConstant;
import com.fanmu.muwu.common.util.support.ThreadLocalMap;
import org.apache.dubbo.rpc.*;

public class PlatformDubboAddUserInfoFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        // 添加用户信息
        Object currentBasicUser = ThreadLocalMap.get(GlobalConstant.Sys.CURRENT_BASIC_USER);
        if (currentBasicUser != null) {
            invocation.setAttachment(GlobalConstant.Sys.CURRENT_BASIC_USER, currentBasicUser);
        }
        return invoker.invoke(invocation);
    }

}
