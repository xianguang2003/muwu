/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class JsonUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 取消默认转换timestamps形式,false使用日期格式转换，true不使用日期转换，结果是时间的数值157113793535
        // objectMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false); //默认值true

        // 忽略空Bean转json的错误
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // 所有的日期格式统一样式： yyyy-MM-dd HH:mm:ss
        // objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));

        // 忽略 在json字符串中存在，但是在对象中不存在对应属性的情况，防止错误。
        // 例如json数据中多出字段，而对象中没有此字段。如果设置true，抛出异常，因为字段不对应；false则忽略多出的字段，默认值为null，将其他字段反序列化成功
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
    }

    //将单个对象转换成json格式的字符串（没有格式化后的json）
    public static <T> String writeValueAsString(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
//            log.warn("Parse object to String error", e);
            return null;
        }
    }

    //将单个对象转换成json格式的字符串（格式化后的json）
    public static <T> String writeValueAsStringPretty(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException e) {
//            log.warn("Parse object to String error", e);
            return null;
        }
    }

    //将json形式的字符串数据转换成单个对象
    public static <T> T readValue(String content, Class<T> valueType) {
        if (StringUtils.isEmpty(content) || valueType == null) {
            return null;
        }
        try {
            return valueType.equals(String.class) ? (T) content : objectMapper.readValue(content, valueType);
        } catch (IOException e) {
//            log.warn("Parse object to Object error", e);
            return null;
        }
    }

    //将json形式的字符串数据转换成多个对象
    public static <T> T readValue(String content, TypeReference<T> valueTypeRef) {
        if (StringUtils.isEmpty(content) || valueTypeRef == null) {
            return null;
        }
        try {
            return valueTypeRef.getType().equals(String.class) ? (T) content : (T) objectMapper.readValue(content, valueTypeRef);
        } catch (IOException e) {
//            log.warn("Parse object to Object error", e);
            return null;
        }
    }

    //将json形式的字符串数据转换成多个对象
    public static <T> T readValue(String content, Class<T> parametrized, Class<?>... parameterClasses) {
        JavaType valueType = objectMapper.getTypeFactory().constructParametricType(parametrized, parameterClasses);
        try {
            return objectMapper.readValue(content, valueType);
        } catch (IOException e) {
//            log.warn("Parse object to Object error", e);
            return null;
        }
    }
}
