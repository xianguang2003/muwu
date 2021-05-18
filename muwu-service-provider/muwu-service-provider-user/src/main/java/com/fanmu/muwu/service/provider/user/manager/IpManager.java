/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.manager;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.fanmu.muwu.service.provider.user.api.model.dto.loginlog.LoginLogInfo;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class IpManager {

    // TODO 后续改为读取配置
    // 高德地图密钥
    private final String KEY = "55d30f968656ed2c9e2ea3764ca1304e";

    public void ipParsing(LoginLogInfo loginLogInfo) {
        Map<String, Object> parameters = Maps.newHashMap();
        parameters.put("key", KEY);
        parameters.put("ip", loginLogInfo.getIp());
        String result = HttpUtil.get("https://restapi.amap.com/v3/ip", parameters);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (jsonObject.getString("status").equals("1")) {
            loginLogInfo.setCountry("中国");
            loginLogInfo.setProvince(jsonObject.getString("province").equals("[]") ? "未知" : jsonObject.getString("province"));
            loginLogInfo.setCity(jsonObject.getString("city").equals("[]") ? "未知" : jsonObject.getString("city"));
        }
    }
}
