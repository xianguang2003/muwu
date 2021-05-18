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

@Data
public class BasicUser implements Serializable {

    private Long userId;

    private String username;

    private String nickname;

}
