/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Map;

/**
 * The class Public util.
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PublicUtil {

	/**
	 * 判断对象是否Empty(null或元素为0)
	 * 实用于对如下对象做判断:String Collection及其子类 Map及其子类
	 *
	 * @param pObj 待检查对象
	 *
	 * @return boolean 返回的布尔值
	 */
	public static boolean isEmpty(Object pObj) {
		if (pObj == null) {
			return true;
		}
		if (pObj == "") {
			return true;
		}
		if (pObj instanceof String) {
			return ((String) pObj).length() == 0;
		} else if (pObj instanceof Collection) {
			return ((Collection) pObj).isEmpty();
		} else if (pObj instanceof Map) {
			return ((Map) pObj).size() == 0;
		}
		return false;
	}

	/**
	 * 判断对象是否为NotEmpty(!null或元素大于0)
	 * 实用于对如下对象做判断:String Collection及其子类 Map及其子类
	 *
	 * @param pObj 待检查对象
	 *
	 * @return boolean 返回的布尔值
	 */
	public static boolean isNotEmpty(Object pObj) {
		if (pObj == null) {
			return false;
		}
		if (pObj == "") {
			return false;
		}
		if (pObj instanceof String) {
			return ((String) pObj).length() != 0;
		} else if (pObj instanceof Collection) {
			return !((Collection) pObj).isEmpty();
		} else if (pObj instanceof Map) {
			return ((Map) pObj).size() != 0;
		}
		return true;
	}

}
