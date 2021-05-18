/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.api.exceptions;

import com.fanmu.muwu.common.base.enums.ErrorCodeEnum;
import com.fanmu.muwu.common.base.exceptions.BusinessException;
import lombok.extern.slf4j.Slf4j;

/**
 * The class biz exception.
 *
 */
@Slf4j
public class UserBizException extends BusinessException {

    private static final long serialVersionUID = -6552248511084911254L;

    /**
     * Instantiates a new rpc exception.
     */
    public UserBizException() {
    }

    /**
     * Instantiates a new rpc exception.
     *
     * @param code      the code
     * @param msgFormat the msg format
     * @param args      the args
     */
    public UserBizException(int code, String msgFormat, Object... args) {
        super(code, msgFormat, args);
        log.info("<== UserRpcException, code:" + this.code + ", message:" + super.getMessage());

    }

    /**
     * Instantiates a new rpc exception.
     *
     * @param code the code
     * @param msg  the msg
     */
    public UserBizException(int code, String msg) {
        super(code, msg);
        log.info("<== UserRpcException, code:" + this.code + ", message:" + super.getMessage());
    }

    /**
     * Instantiates a new rpc exception.
     *
     * @param codeEnum the code enum
     */
    public UserBizException(ErrorCodeEnum codeEnum) {
        super(codeEnum.code(), codeEnum.msg());
        log.info("<== UserRpcException, code:" + this.code + ", message:" + super.getMessage());
    }

    /**
     * Instantiates a new rpc exception.
     *
     * @param codeEnum the code enum
     * @param args     the args
     */
    public UserBizException(ErrorCodeEnum codeEnum, Object... args) {
        super(codeEnum, args);
        log.info("<== UserRpcException, code:" + this.code + ", message:" + super.getMessage());
    }
}
