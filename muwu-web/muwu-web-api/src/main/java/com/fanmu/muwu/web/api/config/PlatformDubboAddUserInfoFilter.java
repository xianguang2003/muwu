/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.web.api.config;

import com.fanmu.muwu.common.base.constant.GlobalConstant;
import com.fanmu.muwu.common.base.pojo.BasicUser;
import com.fanmu.muwu.common.security.SecurityUser;
import com.fanmu.muwu.common.security.SecurityUtils;
import org.apache.dubbo.rpc.*;

public class PlatformDubboAddUserInfoFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        // 添加用户信息
        SecurityUser securityUser = SecurityUtils.getCurrentUser();
        if (securityUser != null) {
            BasicUser basicUser = new BasicUser();
            basicUser.setUserId(securityUser.getUserId());
            basicUser.setUsername(securityUser.getUsername());
            basicUser.setNickname(securityUser.getNickname());
            invocation.setAttachment(GlobalConstant.Sys.CURRENT_BASIC_USER, basicUser);
        }
        return invoker.invoke(invocation);
    }

}
