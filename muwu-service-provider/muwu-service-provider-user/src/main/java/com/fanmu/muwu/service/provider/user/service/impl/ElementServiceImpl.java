/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanmu.muwu.common.base.enums.ErrorCodeEnum;
import com.fanmu.muwu.common.base.enums.GlobalStatusEnum;
import com.fanmu.muwu.common.base.pojo.BaseDTO;
import com.fanmu.muwu.common.coer.extension.service.impl.ServiceImpl;
import com.fanmu.muwu.common.coer.util.MyBatisUtil;
import com.fanmu.muwu.service.provider.user.api.exceptions.UserBizException;
import com.fanmu.muwu.service.provider.user.api.model.dto.element.ElementInfo;
import com.fanmu.muwu.service.provider.user.api.model.query.element.QueryElement;
import com.fanmu.muwu.service.provider.user.api.service.ElementRpcApi;
import com.fanmu.muwu.service.provider.user.mapper.ElementMapper;
import com.fanmu.muwu.service.provider.user.model.domain.Element;
import com.fanmu.muwu.service.provider.user.service.ElementService;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;

/**
 * <p>
 * 页面元素 服务实现类
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
@DubboService(interfaceClass = ElementRpcApi.class)
public class ElementServiceImpl extends ServiceImpl<ElementMapper, Element> implements ElementService {

    @Override
    public void insertElement(ElementInfo elementInfo) {
        Element element = super.getOne(new LambdaQueryWrapper<Element>().eq(Element::getElementCode, elementInfo.getElementCode()));
        if (element != null) {
            throw new UserBizException(ErrorCodeEnum.GL10000003, element.getId());
        }
        element = new Element();
        BeanUtil.copyProperties(elementInfo, element);
        super.save(element);
    }

    @Override
    public void deleteElementById(Long id) {
        Preconditions.checkArgument(id != null, ErrorCodeEnum.GL10000001.msg());

        super.removeById(id);
    }

    @Override
    public void updateElement(ElementInfo elementInfo) {
        Preconditions.checkArgument(elementInfo.getId() != null, ErrorCodeEnum.GL10000001.msg());
        Element element = super.getById(elementInfo.getId());
        if (element == null) {
            throw new UserBizException(ErrorCodeEnum.GL10000002, element.getId());
        }
        // 修改数据
        element = new Element();
        BeanUtil.copyProperties(elementInfo, element);
        super.updateById(element);
    }

    @Override
    public ElementInfo getElementById(Long id) {
        Element element = super.getById(id);
        if (element == null) {
            throw new UserBizException(ErrorCodeEnum.GL10000002, id);
        }

        ElementInfo elementInfo = new ElementInfo();
        BeanUtil.copyProperties(element, elementInfo);
        return elementInfo;
    }

    @Override
    public List<ElementInfo> listElement() {
        return listElement(null);
    }

    @Override
    public List<ElementInfo> listElement(QueryElement queryElement) {
        LambdaQueryWrapper<Element> elementQueryWrapper = new LambdaQueryWrapper<>();
        elementQueryWrapper.eq(Element::getStatus, GlobalStatusEnum.ENABLE.getStatus());
        if (queryElement != null) {
            if (StringUtils.isNotEmpty(queryElement.getKeyword())) {
                elementQueryWrapper.like(Element::getElementName, queryElement.getKeyword())
                        .or().like(Element::getElementCode, queryElement.getKeyword());
            }
        }
        List<Element> elements = super.list(elementQueryWrapper);
        List<ElementInfo> elementInfos = Lists.newArrayList();
        elements.forEach(element -> {
            ElementInfo elementInfo = new ElementInfo();
            BeanUtil.copyProperties(element, elementInfo);
            elementInfos.add(elementInfo);
        });
        return elementInfos;
    }

    @Override
    public BaseDTO<ElementInfo> listElementPage(QueryElement queryElement) {
        Page page = MyBatisUtil.page(queryElement);
        LambdaQueryWrapper<Element> elementQueryWrapper = new LambdaQueryWrapper<>();
        elementQueryWrapper.eq(Element::getStatus, GlobalStatusEnum.ENABLE.getStatus());
        if (StringUtils.isNotEmpty(queryElement.getKeyword())) {
            elementQueryWrapper.like(Element::getElementName, queryElement.getKeyword())
                    .or().like(Element::getElementCode, queryElement.getKeyword());
        }
        IPage<Element> elementIPage = super.page(page, elementQueryWrapper);

        BaseDTO<ElementInfo> baseDTO = MyBatisUtil.returnPage(elementIPage);
        List<ElementInfo> elementInfos = Lists.newArrayList();
        baseDTO.setData(elementInfos);
        elementIPage.getRecords().forEach(element -> {
            ElementInfo elementInfo = new ElementInfo();
            BeanUtil.copyProperties(element, elementInfo);
            elementInfos.add(elementInfo);
        });
        return baseDTO;
    }

}
