/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.security.code.image;

import com.fanmu.muwu.auth.security.code.ValidateCode;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;


/**
 * 图片验证码
 *
 */
public class ImageCode extends ValidateCode {


	private static final long serialVersionUID = -6020470039852318468L;

	private BufferedImage image;

	/**
	 * Instantiates a new Image code.
	 *
	 * @param image    the image
	 * @param code     the code
	 * @param expireIn the expire in
	 */
	ImageCode(BufferedImage image, String code, int expireIn) {
		super(code, expireIn);
		this.image = image;
	}

	/**
	 * Instantiates a new Image code.
	 *
	 * @param image      the image
	 * @param code       the code
	 * @param expireTime the expire time
	 */
	public ImageCode(BufferedImage image, String code, LocalDateTime expireTime) {
		super(code, expireTime);
		this.image = image;
	}

	/**
	 * Gets image.
	 *
	 * @return the image
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * Sets image.
	 *
	 * @param image the image
	 */
	public void setImage(BufferedImage image) {
		this.image = image;
	}

}
