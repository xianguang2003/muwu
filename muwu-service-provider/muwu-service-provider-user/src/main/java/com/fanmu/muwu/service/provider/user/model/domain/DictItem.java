/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.model.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fanmu.muwu.common.coer.extension.model.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 字典项表
 * </p>
 *
 * @author mumu
 * @since 2020-10-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mw_dict_item")
public class DictItem extends BaseModel {

    private static final long serialVersionUID=1L;

    /**
     * 字典ID
     */
    @TableField("dict_id")
    private Long dictId;

    /**
     * 字典项文本
     */
    @TableField("item_text")
    private String itemText;

    /**
     * 字典项值
     */
    @TableField("item_value")
    private String itemValue;

    /**
     * 字典类型0为string,1为number
     */
    @TableField("type")
    private Integer type;

    /**
     * 序号
     */
    @TableField("number")
    private Integer number;

    /**
     * 状态，0可用、1不可用
     */
    @TableField("status")
    private Integer status;

    /**
     * 描述
     */
    @TableField("remark")
    private String remark;


}
