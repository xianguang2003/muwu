/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.api.model.dto.element;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 页面元素
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ElementInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 页面元素名称
     */
    private String elementName;

    /**
     * 页面元素编码
     */
    private String elementCode;

    private String url;

    /**
     * 状态，0可用、1不可用
     */
    private Integer status;

    /**
     * 描述
     */
    private String remark;
}
