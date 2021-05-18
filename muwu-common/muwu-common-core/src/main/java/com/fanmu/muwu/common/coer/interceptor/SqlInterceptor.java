/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.common.coer.interceptor;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.fanmu.muwu.common.base.constant.GlobalConstant;
import com.fanmu.muwu.common.base.pojo.BasicUser;
import com.fanmu.muwu.common.coer.extension.model.domain.BaseModel;
import com.fanmu.muwu.common.util.support.ThreadLocalMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;

import java.sql.Statement;

@Slf4j
@Component
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})})
public class SqlInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 处理创建人或操作人
        Object[] args = invocation.getArgs();
        SqlCommandType sqlCommandType = null;
        for (Object arg : args) {
            // 获取处理类型
            if (arg instanceof MappedStatement) {
                sqlCommandType = ((MappedStatement) arg).getSqlCommandType();
                if (sqlCommandType == SqlCommandType.INSERT || sqlCommandType == SqlCommandType.UPDATE) {
                    continue;
                } else {
                    break;
                }
            }
            // 设置创建人或操作人
            if (arg instanceof MapperMethod.ParamMap) {
                MapperMethod.ParamMap paramMap = (MapperMethod.ParamMap) arg;
                Object et = paramMap.get(Constants.ENTITY);
                if (et != null) {
                    // 处理基础信息
                    basisInfoHandle(et, sqlCommandType);
                }
            }
            // 处理基础信息
            basisInfoHandle(arg, sqlCommandType);
        }
        return invocation.proceed();
    }

    private void basisInfoHandle(Object arg, SqlCommandType sqlCommandType) {
        if (arg instanceof BaseModel) {
            Object currentBasicUser = ThreadLocalMap.get(GlobalConstant.Sys.CURRENT_BASIC_USER);
            if (currentBasicUser != null) {
                BasicUser basicUser = (BasicUser) currentBasicUser;
                if (SqlCommandType.INSERT == sqlCommandType) {
                    ((BaseModel) arg).setCreator(basicUser.getUsername());
                    ((BaseModel) arg).setCreatorId(basicUser.getUserId());
                    ((BaseModel) arg).setLastOperator(basicUser.getUsername());
                    ((BaseModel) arg).setLastOperatorId(basicUser.getUserId());
                } else if (SqlCommandType.UPDATE == sqlCommandType) {
                    ((BaseModel) arg).setLastOperator(basicUser.getUsername());
                    ((BaseModel) arg).setLastOperatorId(basicUser.getUserId());
                }
            }
        }
    }
}
