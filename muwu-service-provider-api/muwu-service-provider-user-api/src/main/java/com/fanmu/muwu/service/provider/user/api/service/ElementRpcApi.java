/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.api.service;

import com.fanmu.muwu.common.base.pojo.BaseDTO;
import com.fanmu.muwu.service.provider.user.api.model.dto.element.ElementInfo;
import com.fanmu.muwu.service.provider.user.api.model.query.element.QueryElement;

import java.util.List;
/**
 * <p>
 * 页面元素 rpc服务类
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
public interface ElementRpcApi {

    void insertElement(ElementInfo elementInfo);

    void deleteElementById(Long id);

    void updateElement(ElementInfo elementInfo);

    ElementInfo getElementById(Long id);

    List<ElementInfo> listElement();

    List<ElementInfo> listElement(QueryElement queryElement);

    BaseDTO<ElementInfo> listElementPage(QueryElement queryElement);

}
