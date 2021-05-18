/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.enums;


/**
 * The enum login type enum.
 */
public enum LoginTypeEnum {
    /**
     * 账号
     */
    ACCOUNT(0, "account", "账号"),
    /**
     * 手机号
     */
    MOBILE(1, "mobile", "手机号"),
    /**
     * 微信
     */
    WEIXIN(2, "weixin", "微信"),
    /**
     * qq
     */
    QQ(3, "qq", "QQ");

    /**
     * The index.
     */
    Integer index;
    /**
     * The type.
     */
    String type;
    /**
     * The Name.
     */
    String name;

    LoginTypeEnum(Integer index, String type, String name) {
        this.index = index;
        this.type = type;
        this.name = name;
    }

    /**
     * Gets index.
     *
     * @return the index
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }


    /**
     * Gets name.
     *
     * @param type the type
     * @return the name
     */
    public static String getName(String type) {
        for (LoginTypeEnum ele : LoginTypeEnum.values()) {
            if (type.equals(ele.getType())) {
                return ele.getName();
            }
        }
        return null;
    }

    /**
     * Gets enum.
     *
     * @param type the type
     * @return the enum
     */
    public static LoginTypeEnum getEnum(String type) {
        for (LoginTypeEnum ele : LoginTypeEnum.values()) {
            if (type.equals(ele.getType())) {
                return ele;
            }
        }
        return null;
    }
}
