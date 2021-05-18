/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.fanmu.muwu.service.provider.user.api.model.dto.loginlog.*;
import com.fanmu.muwu.service.provider.user.api.service.LoginLogRpcApi;
import com.fanmu.muwu.service.provider.user.manager.IpManager;
import com.fanmu.muwu.service.provider.user.model.elasticsearch.LoginLog;
import com.fanmu.muwu.service.provider.user.repository.elasticsearch.LoginLogRepository;
import com.fanmu.muwu.service.provider.user.service.LoginLogService;
import com.google.common.collect.Lists;
import org.apache.dubbo.config.annotation.DubboService;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@DubboService(interfaceClass = LoginLogRpcApi.class)
public class LoginLogServiceImpl implements LoginLogService {

    @Autowired
    LoginLogRepository loginLogRepository;

    @Autowired
    ElasticsearchOperations elasticsearchOperations;

    @Autowired
    IpManager ipManager;

    @Override
    public void save(LoginLogInfo loginLogInfo) {
        ipManager.ipParsing(loginLogInfo);
        LoginLog loginLog = new LoginLog();
        BeanUtil.copyProperties(loginLogInfo, loginLog);
        loginLogRepository.save(loginLog);
    }

    @Override
    public LoginLogStatistics statisticsData() {
        // 省份统计
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withQuery(QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("login_time").gte(DateUtil.lastWeek().toJdkDate())));
        searchQueryBuilder.addAggregation(AggregationBuilders.terms("provinceAggregation").field("province.keyword"));
        SearchHits<LoginLog> hits = elasticsearchOperations.search(searchQueryBuilder.build(), LoginLog.class);
        Aggregations aggregations = hits.getAggregations();
        ParsedStringTerms provinceAggregation = aggregations.get("provinceAggregation");
        List<? extends Terms.Bucket> buckets = provinceAggregation.getBuckets();
        List<LoginLogAggregation> province = Lists.newArrayList();
        buckets.forEach(bucket -> {
            LoginLogAggregation loginLogAggregation = new LoginLogAggregation();
            loginLogAggregation.setName(bucket.getKeyAsString());
            loginLogAggregation.setValue(bucket.getDocCount());
            province.add(loginLogAggregation);
        });
        LoginLogProvinceStatistics provinceStatistics = new LoginLogProvinceStatistics();
        provinceStatistics.setTitleData(province.stream().map(LoginLogAggregation::getName).collect(Collectors.toList()));
        provinceStatistics.setData(province);

        // 登录总量统计
        searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withQuery(QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("login_time").gte(DateUtil.lastWeek().toJdkDate())));
        searchQueryBuilder.addAggregation(AggregationBuilders.terms("loginTypeTotalAggregation").field("login_type").
                subAggregation(AggregationBuilders.dateHistogram("loginTimeTotalAggregation").field("login_time").fixedInterval(DateHistogramInterval.DAY).format(DatePattern.NORM_DATE_PATTERN)));
        hits = elasticsearchOperations.search(searchQueryBuilder.build(), LoginLog.class);
        aggregations = hits.getAggregations();
        ParsedStringTerms loginTypeTotalAggregation = aggregations.get("loginTypeTotalAggregation");
        List<LoginLogLoginTypeStatistics> loginTypeTotal = Lists.newArrayList();
        loginTypeTotalAggregation.getBuckets().forEach(loginTypeBucket -> {
            LoginLogLoginTypeStatistics loginLogLoginTypeStatistics = new LoginLogLoginTypeStatistics();
            loginLogLoginTypeStatistics.setTypeName(loginTypeBucket.getKeyAsString());
            loginLogLoginTypeStatistics.setCount(loginTypeBucket.getDocCount());
            Aggregations subAggregation = loginTypeBucket.getAggregations();
            ParsedDateHistogram loginTimeTotalAggregation = subAggregation.get("loginTimeTotalAggregation");
            List<LoginLogAggregation> loginTimeTotal = Lists.newArrayList();
            loginTimeTotalAggregation.getBuckets().forEach(loginTimeBucket -> {
                LoginLogAggregation loginLogAggregation = new LoginLogAggregation();
                loginLogAggregation.setName(loginTimeBucket.getKeyAsString());
                loginLogAggregation.setValue(loginTimeBucket.getDocCount());
                loginTimeTotal.add(loginLogAggregation);
            });
            loginLogLoginTypeStatistics.setData(loginTimeTotal);
            loginTypeTotal.add(loginLogLoginTypeStatistics);
        });

        // 登录成功总量统计
        searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withQuery(QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("login_time").gte(DateUtil.lastWeek().toJdkDate())).
                must(QueryBuilders.termQuery("status", 0)));
        searchQueryBuilder.addAggregation(AggregationBuilders.terms("loginTypeSuccessAggregation").field("login_type").
                subAggregation(AggregationBuilders.dateHistogram("loginTimeSuccessAggregation").field("login_time").fixedInterval(DateHistogramInterval.DAY).format(DatePattern.NORM_DATE_PATTERN)));
        hits = elasticsearchOperations.search(searchQueryBuilder.build(), LoginLog.class);
        aggregations = hits.getAggregations();
        ParsedStringTerms loginTypeSuccessAggregation = aggregations.get("loginTypeSuccessAggregation");
        List<LoginLogLoginTypeStatistics> loginTypeSuccess = Lists.newArrayList();
        loginTypeSuccessAggregation.getBuckets().forEach(loginTypeBucket -> {
            LoginLogLoginTypeStatistics loginLogLoginTypeStatistics = new LoginLogLoginTypeStatistics();
            loginLogLoginTypeStatistics.setTypeName(loginTypeBucket.getKeyAsString());
            loginLogLoginTypeStatistics.setCount(loginTypeBucket.getDocCount());
            Aggregations subAggregation = loginTypeBucket.getAggregations();
            ParsedDateHistogram loginTimeSuccessAggregation = subAggregation.get("loginTimeSuccessAggregation");
            List<LoginLogAggregation> loginTimeSuccess = Lists.newArrayList();
            loginTimeSuccessAggregation.getBuckets().forEach(loginTimeBucket -> {
                LoginLogAggregation loginLogAggregation = new LoginLogAggregation();
                loginLogAggregation.setName(loginTimeBucket.getKeyAsString());
                loginLogAggregation.setValue(loginTimeBucket.getDocCount());
                loginTimeSuccess.add(loginLogAggregation);
            });
            loginLogLoginTypeStatistics.setData(loginTimeSuccess);
            loginTypeSuccess.add(loginLogLoginTypeStatistics);
        });

        // 登录失败总量统计
        searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withQuery(QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("login_time").gte(DateUtil.lastWeek().toJdkDate())).
                must(QueryBuilders.termQuery("status", 1)));
        searchQueryBuilder.addAggregation(AggregationBuilders.terms("loginTypeFailureAggregation").field("login_type").
                subAggregation(AggregationBuilders.dateHistogram("loginTimeFailureAggregation").field("login_time").fixedInterval(DateHistogramInterval.DAY).format(DatePattern.NORM_DATE_PATTERN)));
        hits = elasticsearchOperations.search(searchQueryBuilder.build(), LoginLog.class);
        aggregations = hits.getAggregations();
        ParsedStringTerms loginTypeFailureAggregation = aggregations.get("loginTypeFailureAggregation");
        List<LoginLogLoginTypeStatistics> loginTypeFailure = Lists.newArrayList();
        loginTypeFailureAggregation.getBuckets().forEach(loginTypeBucket -> {
            LoginLogLoginTypeStatistics loginLogLoginTypeStatistics = new LoginLogLoginTypeStatistics();
            loginLogLoginTypeStatistics.setTypeName(loginTypeBucket.getKeyAsString());
            loginLogLoginTypeStatistics.setCount(loginTypeBucket.getDocCount());
            Aggregations subAggregation = loginTypeBucket.getAggregations();
            ParsedDateHistogram loginTimeFailureAggregation = subAggregation.get("loginTimeFailureAggregation");
            List<LoginLogAggregation> loginTimeFailure = Lists.newArrayList();
            loginTimeFailureAggregation.getBuckets().forEach(loginTimeBucket -> {
                LoginLogAggregation loginLogAggregation = new LoginLogAggregation();
                loginLogAggregation.setName(loginTimeBucket.getKeyAsString());
                loginLogAggregation.setValue(loginTimeBucket.getDocCount());
                loginTimeFailure.add(loginLogAggregation);
            });
            loginLogLoginTypeStatistics.setData(loginTimeFailure);
            loginTypeFailure.add(loginLogLoginTypeStatistics);
        });

        // 计算数据
        List<LoginLogLoginTypeData> loginLogLoginTypeData = Lists.newArrayList();
        String[] loginTypes = {"account", "mobile", "weixin", "qq"};
        List<String> dateTitle = Lists.newArrayList();
        Date lastWeek = DateUtil.lastWeek().toJdkDate();
        long betweenDay = DateUtil.betweenDay(lastWeek, new Date(), true);
        for (int i = 1; i <= betweenDay; i++) {
            dateTitle.add(DateUtil.offsetDay(lastWeek, i).toString(DatePattern.NORM_DATE_FORMAT));
        }
        for (String loginType : loginTypes) {
            long value;
            long total = 0;
            List<Long> totalData = Lists.newArrayList();
            for (LoginLogLoginTypeStatistics loginLogLoginTypeStatistics : loginTypeTotal) {
                if (loginLogLoginTypeStatistics.getTypeName().equals(loginType)) {
                    total = loginLogLoginTypeStatistics.getCount();
                    List<LoginLogAggregation> data = loginLogLoginTypeStatistics.getData();
                    for (String title : dateTitle) {
                        value = 0L;
                        for (LoginLogAggregation aggregation : data) {
                            if (title.equals(aggregation.getName())) {
                                value = aggregation.getValue();
                            }
                        }
                        totalData.add(value);
                    }
                }
            }
            if (totalData.isEmpty()) {
                for (int i = 0; i < dateTitle.size(); i++) {
                    totalData.add(0L);
                }
            }
            long success = 0;
            List<Long> successData = Lists.newArrayList();
            for (LoginLogLoginTypeStatistics loginLogLoginTypeStatistics : loginTypeSuccess) {
                if (loginLogLoginTypeStatistics.getTypeName().equals(loginType)) {
                    success = loginLogLoginTypeStatistics.getCount();
                    List<LoginLogAggregation> data = loginLogLoginTypeStatistics.getData();
                    for (String title : dateTitle) {
                        value = 0L;
                        for (LoginLogAggregation aggregation : data) {
                            if (title.equals(aggregation.getName())) {
                                value = aggregation.getValue();
                            }
                        }
                        successData.add(value);
                    }
                }
            }
            if (successData.isEmpty()) {
                for (int i = 0; i < dateTitle.size(); i++) {
                    successData.add(0L);
                }
            }
            long failure = 0;
            List<Long> failureData = Lists.newArrayList();
            for (LoginLogLoginTypeStatistics loginLogLoginTypeStatistics : loginTypeFailure) {
                if (loginLogLoginTypeStatistics.getTypeName().equals(loginType)) {
                    failure = loginLogLoginTypeStatistics.getCount();
                    List<LoginLogAggregation> data = loginLogLoginTypeStatistics.getData();
                    for (String title : dateTitle) {
                        value = 0L;
                        for (LoginLogAggregation aggregation : data) {
                            if (title.equals(aggregation.getName())) {
                                value = aggregation.getValue();
                            }
                        }
                        failureData.add(value);
                    }
                }
            }
            if (failureData.isEmpty()) {
                for (int i = 0; i < dateTitle.size(); i++) {
                    failureData.add(0L);
                }
            }
            LoginLogLoginTypeData loginTypeData = new LoginLogLoginTypeData();
            loginTypeData.setTypeName(loginType);
            loginTypeData.setTitleData(dateTitle);
            loginTypeData.setTotal(total);
            loginTypeData.setSuccess(success);
            loginTypeData.setFailure(failure);
            loginTypeData.setTotalData(totalData);
            loginTypeData.setSuccessData(successData);
            loginTypeData.setFailureData(failureData);
            loginLogLoginTypeData.add(loginTypeData);
        }
        // 组装数据
        LoginLogStatistics loginLogStatistics = new LoginLogStatistics();
        loginLogStatistics.setProvinceStatistics(provinceStatistics);
        loginLogStatistics.setLoginTypeTotalStatistics(loginTypeTotal);
        loginLogStatistics.setLoginTypeSuccessStatistics(loginTypeSuccess);
        loginLogStatistics.setLoginTypeFailureStatistics(loginTypeFailure);
        loginLogStatistics.setLoginLogLoginTypeData(loginLogLoginTypeData);
        loginLogStatistics.setLoginLogLoginTitleData(dateTitle);
        return loginLogStatistics;
    }

}
