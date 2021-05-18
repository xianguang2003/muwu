/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.common.base.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * The class Base DTO.
 *
 */
@Data
public class BaseDTO<T> implements Serializable {

    //当前页
    private Long pageNum;
    //每页的数量
    private Long pageSize;
    //总记录数
    private Long total;
    //总页数
    private Long pages;
    //结果集
    private List<T> data;

}
