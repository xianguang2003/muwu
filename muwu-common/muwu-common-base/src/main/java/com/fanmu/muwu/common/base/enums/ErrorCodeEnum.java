/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.common.base.enums;


/**
 * The class Error code enum.
 *
 */
public enum ErrorCodeEnum {

	// 全局统一业务异常
	/**
	 * Gl 10000001 error code enum.
	 */
	GL10000001(10000001, "ID不能为空"),
	/**
	 * Gl 10000002 error code enum.
	 */
	GL10000002(10000002, "找不到信息, ID=%s"),
	// 认证
	/**
	 * AUTH 10000001 error code enum.
	 */
	AUTH10000001(10000001, "页面已过期,请重新登录"),
	// 用户
	/**
	 * USER 10011001 error code enum.
	 */
	USER10011001(10011001, "用户名不能为空"),
	/**
	 * USER 10011012 error code enum.
	 */
	USER10011002(10011012, "密码不能为空"),
	/**
	 * USER 10011013 error code enum.
	 */
	USER10011013(10011013, "用户名已存在"),
	/**
	 * USER 10011014 error code enum.
	 */
	USER10011014(10011014, "手机号已存在"),
	/**
	 * USER 10011015 error code enum.
	 */
	USER10011015(10011015, "邮箱已存在"),
	/**
	 * USER 10011016 error code enum.
	 */
	USER10011016(10011016, "用户Id不能为空"),
	/**
	 * USER 10011017 error code enum.
	 */
	USER10011017(10011017, "用户不存在, userId=%s"),
	/**
	 * USER 10011018 error code enum.
	 */
	USER10011018(10011018, "用户Id不能为空"),
	// 角色
	/**
	 * USER 10012001 error code enum.
	 */
	USER10012001(10012001, "角色ID不能为空"),
	/**
	 * USER 10012002 error code enum.
	 */
	USER10012002(10012002, "找不到角色信息,roleId=%s"),
	// 权限
	/**
	 * USER 10013001 error code enum.
	 */
	USER10013001(10013001, "权限code已存在, permissionId=%s"),
	/**
	 * USER 10013002 error code enum.
	 */
	USER10013002(10013002, "权限ID不能为空"),
	/**
	 * USER 10013003 error code enum.
	 */
	USER10013003(10013003, "权限ID不能为空"),
	// 菜单
	/**
	 * USER 10014001 error code enum.
	 */
	USER10014001(10014001, "父菜单不存在,menuId=%s"),
	/**
	 * USER 10014002 error code enum.
	 */
	USER10014002(10014002, "最大支持三级菜单"),
	/**
	 * USER 10014003 error code enum.
	 */
	USER10014003(10014003, "菜单不存在,menuId=%s"),
	/**
	 * USER 10014004 error code enum.
	 */
	USER10014004(10014004, "请先删除子菜单,menuId=%s"),
	/**
	 * USER 10014005 error code enum.
	 */
	USER10014005(10014005, "菜单ID不能为空"),


	/**
	 * Gl 10000003 error code enum.
	 */
	GL10000003(10000003, "已存在, ID=%s");

	private int code;
	private String msg;

	/**
	 * Msg string.
	 *
	 * @return the string
	 */
	public String msg() {
		return msg;
	}

	/**
	 * Code int.
	 *
	 * @return the int
	 */
	public int code() {
		return code;
	}

	ErrorCodeEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	/**
	 * Gets enum.
	 *
	 * @param code the code
	 *
	 * @return the enum
	 */
	public static ErrorCodeEnum getEnum(int code) {
		for (ErrorCodeEnum ele : ErrorCodeEnum.values()) {
			if (ele.code() == code) {
				return ele;
			}
		}
		return null;
	}
}
