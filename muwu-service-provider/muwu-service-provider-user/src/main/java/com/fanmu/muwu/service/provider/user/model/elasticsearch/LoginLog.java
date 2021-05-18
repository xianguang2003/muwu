/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.model.elasticsearch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.util.Date;

@Data
@TypeAlias("UcLoginLog")
@Document(indexName = LoginLog.INDEX)
public class LoginLog {

    public static final String INDEX = "login_log";

    @Id
    private String id;

    @Field("login_type")
    @JsonProperty("login_type")
    private String loginType;

    @Field("type")
    @JsonProperty("type")
    private String type;

    @Field("username")
    @JsonProperty("username")
    private String username;

    @Field("os")
    @JsonProperty("os")
    private String os;

    @Field("browser")
    @JsonProperty("browser")
    private String browser;

    @Field("ip")
    @JsonProperty("ip")
    private String ip;

    @Field("terminal")
    @JsonProperty("terminal")
    private String terminal;

    @Field("country")
    @JsonProperty("country")
    private String country;

    @Field("province")
    @JsonProperty("province")
    private String province;

    @Field("city")
    @JsonProperty("city")
    private String city;

    @Field("location")
    @JsonProperty("location")
    private String location;

    @Field("status")
    @JsonProperty("status")
    private Integer status;

    @Field("description")
    @JsonProperty("description")
    private String description;

    @Field("login_time")
    @JsonProperty("login_time")
    private Date loginTime;
}
