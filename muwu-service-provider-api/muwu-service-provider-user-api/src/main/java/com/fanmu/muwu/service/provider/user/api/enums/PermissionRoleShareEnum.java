/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.api.enums;

public enum PermissionRoleShareEnum {

    /**
     * 是
     */
    YES(1, "是"),
    /**
     * 不是
     */
    NO(0, "不是");

    /**
     * The value.
     */
    Integer value;
    /**
     * The Name.
     */
    String name;

    PermissionRoleShareEnum(Integer value, String name) {
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
        for (PermissionRoleShareEnum ele : PermissionRoleShareEnum.values()) {
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
    public static PermissionRoleShareEnum getEnum(Integer status) {
        for (PermissionRoleShareEnum ele : PermissionRoleShareEnum.values()) {
            if (status.equals(ele.getValue())) {
                return ele;
            }
        }
        return PermissionRoleShareEnum.YES;
    }
}
