/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.common.coer.extension.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * The class Base entity.
 *
 */
@Data
public class BaseModel implements Serializable {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @TableField("creator")
    private String creator;

    /**
     * 创建人ID
     */
    @TableField("creator_id")
    private Long creatorId;


    /**
     * 更新时间
     */
    @TableField("update_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 操作人
     */
    @TableField("last_operator")
    private String lastOperator;

    /**
     * 操作人ID
     */
    @TableField("last_operator_id")
    private Long lastOperatorId;

    /**
     * 逻辑删除，1 表示删除、0 表示未删除
     */
    @TableField("is_deleted")
    @TableLogic
    private Boolean isDeleted;

    @TableField(exist = false)
    private Integer pageNum;

    @TableField(exist = false)
    private Integer pageSize;

    @TableField(exist = false)
    private String orderBy;

    /**
     * Is new boolean.
     *
     * @return the boolean
     */
    @JsonIgnore
    public boolean isNew() {
        return this.id == null;
    }

}
