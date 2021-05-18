/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.api.model.dto.dictitem;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 字典项表
 * </p>
 *
 * @author mumu
 * @since 2020-10-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DictItemInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 字典ID
     */
    private Long dictId;

    /**
     * 字典项文本
     */
    private String itemText;

    /**
     * 字典项值
     */
    private String itemValue;

    /**
     * 字典类型0为string,1为number
     */
    private Integer type;

    /**
     * 序号
     */
    private Integer number;

    /**
     * 状态，0可用、1不可用
     */
    private Integer status;

    /**
     * 描述
     */
    private String remark;
}
