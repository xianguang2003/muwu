/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.common.util;

import cn.hutool.core.codec.Base64;
import com.fanmu.muwu.common.base.constant.GlobalConstant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.crypto.codec.Base64;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * The class Request util.
 *
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestUtil {

	/**
	 * Gets request.
	 *
	 * @return the request
	 */
	/*public static HttpServletRequest getRequest() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return attributes == null ? null : attributes.getRequest();
	}*/

	/**
	 * 获得用户远程地址
	 *
	 * @param request the request
	 *
	 * @return the string
	 */
	public static String getRemoteAddr(HttpServletRequest request) {
		String ipAddress = request.getHeader(GlobalConstant.X_REAL_IP);
		if (StringUtils.isEmpty(ipAddress) || GlobalConstant.UNKNOWN.equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader(GlobalConstant.X_FORWARDED_FOR);
		}
		if (StringUtils.isEmpty(ipAddress) || GlobalConstant.UNKNOWN.equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader(GlobalConstant.PROXY_CLIENT_IP);
		}
		if (StringUtils.isEmpty(ipAddress) || GlobalConstant.UNKNOWN.equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader(GlobalConstant.WL_PROXY_CLIENT_IP);
		}
		if (StringUtils.isEmpty(ipAddress) || GlobalConstant.UNKNOWN.equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader(GlobalConstant.HTTP_CLIENT_IP);
		}
		if (StringUtils.isEmpty(ipAddress) || GlobalConstant.UNKNOWN.equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader(GlobalConstant.HTTP_X_FORWARDED_FOR);
		}
		if (StringUtils.isEmpty(ipAddress) || GlobalConstant.UNKNOWN.equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
		}
		if (StringUtils.isEmpty(ipAddress) || GlobalConstant.UNKNOWN.equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (GlobalConstant.LOCALHOST_IP.equals(ipAddress) || GlobalConstant.LOCALHOST_IP_16.equals(ipAddress)) {
				//根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					log.error("获取IP地址, 出现异常={}", e.getMessage(), e);
				}
				assert inet != null;
				ipAddress = inet.getHostAddress();
			}
			log.info("获取IP地址 ipAddress={}", ipAddress);
		}
		// 对于通过多个代理的情况, 第一个IP为客户端真实IP,多个IP按照','分割 //"***.***.***.***".length() = 15
		if (ipAddress != null && ipAddress.length() > GlobalConstant.MAX_IP_LENGTH) {
			if (ipAddress.indexOf(GlobalConstant.Symbol.COMMA) > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(GlobalConstant.Symbol.COMMA));
			}
		}
		return ipAddress;
	}

	public static String[] extractAndDecodeHeader(String header) throws IOException {

		byte[] base64Token = header.substring(6).getBytes("UTF-8");
		byte[] decoded = Base64.decode(base64Token);
		String token = new String(decoded, "UTF-8");

		int delim = token.indexOf(GlobalConstant.Symbol.MH);
		if (delim == -1) {
			throw new IOException("Invalid basic authentication token");
		}
		return new String[]{token.substring(0, delim), token.substring(delim + 1)};
	}
}
