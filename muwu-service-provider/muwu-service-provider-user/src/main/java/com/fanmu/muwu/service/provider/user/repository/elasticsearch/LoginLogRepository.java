/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.repository.elasticsearch;

import com.fanmu.muwu.service.provider.user.model.elasticsearch.LoginLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface LoginLogRepository extends ElasticsearchRepository<LoginLog, String> {

}
