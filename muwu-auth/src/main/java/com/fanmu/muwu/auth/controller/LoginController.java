/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.controller;

import com.fanmu.muwu.common.base.enums.ErrorCodeEnum;
import com.fanmu.muwu.common.web.extension.wrapper.WrapMapper;
import com.fanmu.muwu.common.web.extension.wrapper.Wrapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    @RequestMapping("/auth/login")
    public String authLogin() {
        return "login";
    }

    @ResponseBody
    @RequestMapping("/oauth/login")
    public Wrapper login() {
        return WrapMapper.wrap(ErrorCodeEnum.AUTH10000001.code(), ErrorCodeEnum.AUTH10000001.msg());
    }

    @ResponseBody
    @RequestMapping("/oauth/test")
    public Wrapper test() {
        return WrapMapper.ok("测试地址");
    }

}
