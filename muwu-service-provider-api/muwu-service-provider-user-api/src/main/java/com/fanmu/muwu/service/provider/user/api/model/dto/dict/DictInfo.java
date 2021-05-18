/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.api.model.dto.dict;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 字典
 * </p>
 *
 * @author mumu
 * @since 2020-10-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DictInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典编码
     */
    private String dictCode;

    /**
     * 状态，0可用、1不可用
     */
    private Integer status;

    /**
     * 描述
     */
    private String remark;
}
