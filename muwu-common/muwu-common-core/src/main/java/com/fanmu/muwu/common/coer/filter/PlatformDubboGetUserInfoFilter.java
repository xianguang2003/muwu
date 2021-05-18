/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.common.coer.filter;

import com.fanmu.muwu.common.base.constant.GlobalConstant;
import com.fanmu.muwu.common.base.pojo.BasicUser;
import com.fanmu.muwu.common.util.support.ThreadLocalMap;
import org.apache.dubbo.rpc.*;

public class PlatformDubboGetUserInfoFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Object attachment = invocation.getObjectAttachment(GlobalConstant.Sys.CURRENT_BASIC_USER);
        if (attachment != null) {
            BasicUser basicUser = (BasicUser) attachment;
            ThreadLocalMap.put(GlobalConstant.Sys.CURRENT_BASIC_USER, basicUser);
        }
        return invoker.invoke(invocation);
    }

}
