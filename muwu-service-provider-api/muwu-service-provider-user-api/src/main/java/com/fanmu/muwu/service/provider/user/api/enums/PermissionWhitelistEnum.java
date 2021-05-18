/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.api.enums;

public enum PermissionWhitelistEnum {

    /**
     * 不需要权限验证
     */
    YES(1, "不需要权限验证"),
    /**
     * 需要权限验证
     */
    NO(0, "需要权限验证");

    /**
     * The value.
     */
    Integer value;
    /**
     * The Name.
     */
    String name;

    PermissionWhitelistEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public Integer getValue() {
        return value;
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
     * @param status the status
     * @return the name
     */
    public static String getName(Integer status) {
        for (PermissionWhitelistEnum ele : PermissionWhitelistEnum.values()) {
            if (status.equals(ele.getValue())) {
                return ele.getName();
            }
        }
        return null;
    }

    /**
     * Gets enum.
     *
     * @param status the status
     * @return the enum
     */
    public static PermissionWhitelistEnum getEnum(Integer status) {
        for (PermissionWhitelistEnum ele : PermissionWhitelistEnum.values()) {
            if (status.equals(ele.getValue())) {
                return ele;
            }
        }
        return PermissionWhitelistEnum.YES;
    }
}
