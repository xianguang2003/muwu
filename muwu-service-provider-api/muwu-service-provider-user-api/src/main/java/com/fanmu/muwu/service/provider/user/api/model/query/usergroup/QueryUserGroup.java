/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.api.model.query.usergroup;

import com.fanmu.muwu.common.base.pojo.BaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Builder;

/**
 * <p>
 * 用户组
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class QueryUserGroup extends BaseQuery {

    private Long id;

    private String keyword;

}
