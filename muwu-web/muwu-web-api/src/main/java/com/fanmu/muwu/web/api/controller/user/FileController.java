/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 *
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.web.api.controller.user;

import cn.hutool.core.date.DateUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.fanmu.muwu.common.base.constant.GlobalConstant;
import com.fanmu.muwu.common.config.properties.MuWuProperties;
import com.fanmu.muwu.common.config.properties.OssProperties;
import com.fanmu.muwu.common.web.extension.wrapper.WrapMapper;
import com.fanmu.muwu.common.web.extension.wrapper.Wrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * <p>
 * 文件管理 前端控制器
 * </p>
 *
 * @author mumu
 * @since 2020-07-31
 */
@RestController
@RequestMapping(value = "/file", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "Web - UserController", tags = {"文件管理"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class FileController {

    @Autowired
    MuWuProperties muWuProperties;

    String[] imgTypes = {"image/jpeg", "image/png"};

    @PostMapping(value = "/fileUpload")
    @ApiOperation(value = "证照上传")
    public Wrapper<String> fileUpload(@RequestParam(value = "file") MultipartFile file,
                                      @RequestParam(value = "name", required = false) String name,
                                      @RequestParam(value = "path", required = false) String path,
                                      @RequestParam(value = "resultFullPath", required = false, defaultValue = "false") Boolean resultFullPath,
                                      @RequestParam(value = "type", required = false, defaultValue = "file") String type) throws IOException {
        if (file.isEmpty()) {
            System.out.println("文件为空空");
        }
        if (file.getSize() / 1000 > 4096) {
            return WrapMapper.error("文件大小不能超过4MB");
        }
        //判断上传文件格式
        if (StringUtils.isNotEmpty(type)) {
            boolean isType = false;
            String fileType = file.getContentType();
            if (type.equals("img")) {
                for (String imgType : imgTypes) {
                    if (fileType.equals(imgType)) {
                        isType = true;
                    }
                }
            }
            if (type.equals("file")) {
                // 默认文件类型都支持
                isType = true;
            }
            if (!isType) {
                return WrapMapper.error("占不支持文件类型");
            }
        }

        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        if (StringUtils.isNotEmpty(name)) {
            fileName = name + suffixName; // 新文件名
        } else {
            fileName = UUID.randomUUID().toString() + suffixName; // 新文件名
        }

        StringBuilder filePath = new StringBuilder();
        if (StringUtils.isNotEmpty(path)) {
            filePath.append(path);
        } else {
            if (StringUtils.isNotEmpty(type)) {
                if (StringUtils.isNotBlank(filePath) && !filePath.toString().endsWith(GlobalConstant.Symbol.SLASH)) {
                    filePath.append(GlobalConstant.Symbol.SLASH);
                }
                filePath.append(type);
            }
            if (StringUtils.isNotBlank(filePath) && !filePath.toString().endsWith(GlobalConstant.Symbol.SLASH)) {
                filePath.append(GlobalConstant.Symbol.SLASH);
            }
            filePath.append(DateUtil.today());
        }
        if (StringUtils.isEmpty(filePath)) {
            filePath.append(GlobalConstant.Oss.DEFAULT_FILE_PATH);
        }
        if (StringUtils.isNotBlank(filePath) && !filePath.toString().endsWith(GlobalConstant.Symbol.SLASH)) {
            filePath.append(GlobalConstant.Symbol.SLASH);
        }
        filePath.append(fileName);

        OssProperties oss = muWuProperties.getOss();
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(oss.getEndpoint(), oss.getAccessKeyId(), oss.getAccessKeySecret());
        ossClient.putObject(oss.getBucketName(), filePath.toString(), file.getInputStream());
        // 关闭OSSClient。
        ossClient.shutdown();

        if (resultFullPath) {
            filePath.insert(0, muWuProperties.getOss().getGlobalPath());
        }

        return WrapMapper.ok(filePath.toString());
    }

    @PostMapping(value = "/deleteFile")
    @ApiOperation(value = "读取图片流")
    public Wrapper deleteFile(String path) {
        OssProperties oss = muWuProperties.getOss();
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(oss.getEndpoint(), oss.getAccessKeyId(), oss.getAccessKeySecret());
        ossClient.deleteObject(oss.getBucketName(), path);
        // 关闭OSSClient。
        ossClient.shutdown();
        return WrapMapper.ok();
    }

    @PostMapping(value = "/getFilePath")
    @ApiOperation(value = "获取文件绝对地址")
    public Wrapper<String> getFilePath(String path) {
        return WrapMapper.ok(muWuProperties.getOss().getGlobalPath() + path);
    }


}
