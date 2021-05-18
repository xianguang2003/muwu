/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.api.model.dto.user;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * The class Auth user dto.
 *
 */
@Data
public class AuthUser implements Serializable {
	private static final long serialVersionUID = -404960546624024001L;

	private Long userId;

	private String nickname;

	private String username;

	private String password;

	private String status;

	private Long groupId;

	private String groupName;

	List<String> roles;
}
