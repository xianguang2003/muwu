/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.common.base.enums;


/**
 * The enum menu status enum.
 */
public enum GlobalStatusEnum {
    /**
     * 启用
     */
    ENABLE(0, "启用"),
    /**
     * 禁用
     */
    DISABLE(1, "禁用");

    /**
     * The status.
     */
    Integer status;
    /**
     * The Name.
     */
    String name;

    GlobalStatusEnum(Integer status, String name) {
        this.status = status;
        this.name = name;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public Integer getStatus() {
        return status;
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
        for (GlobalStatusEnum ele : GlobalStatusEnum.values()) {
            if (status.equals(ele.getStatus())) {
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
    public static GlobalStatusEnum getEnum(Integer status) {
        for (GlobalStatusEnum ele : GlobalStatusEnum.values()) {
            if (status.equals(ele.getStatus())) {
                return ele;
            }
        }
        return GlobalStatusEnum.ENABLE;
    }
}
