/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.service.impl;


import com.fanmu.muwu.service.provider.user.model.domain.UserGroupUserRelation;
import com.fanmu.muwu.service.provider.user.mapper.UserGroupUserRelationMapper;
import com.fanmu.muwu.service.provider.user.service.UserGroupUserRelationService;
import com.fanmu.muwu.common.coer.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户组与用户关联 服务实现类
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
@Service
public class UserGroupUserRelationServiceImpl extends ServiceImpl<UserGroupUserRelationMapper, UserGroupUserRelation> implements UserGroupUserRelationService {

}
