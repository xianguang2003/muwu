/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.FileType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MyBatis-Plus 的代码生成器
 */
public class Generator {

    /**
     * 测试 run 执行 注意：不生成service接口 注意：不生成service接口 注意：不生成service接口
     */
    public static void main(String[] args) throws Exception {
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
//        gc.setOutputDir("E:\\mumu");
        gc.setFileOverride(true);
        gc.setActiveRecord(false);
        // XML 二级缓存
        gc.setEnableCache(false);
        // XML ResultMap
        gc.setBaseResultMap(true);
        // XML columList
        gc.setBaseColumnList(true);
        gc.setOpen(false);
        // 作者
        gc.setAuthor("mumu");
        // 默认前面会有I
        gc.setServiceName("%sService");

//        gc.setSwagger2(true);
//        gc.setActiveRecord(true);
        mpg.setGlobalConfig(gc);
        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("fanmu");
        dsc.setPassword("Qwer+1234");
//        dsc.setUrl("jdbc:mysql://47.98.182.53:3306/mw_user_center?characterEncoding=utf8&useSSL=false");
        dsc.setUrl("jdbc:mysql://172.16.34.85:3306/mw_user_center?characterEncoding=utf8&useSSL=false");
        mpg.setDataSource(dsc);
        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // 此处可以修改为您的表前缀
        strategy.setTablePrefix("mw_");
        // 表名生成策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        // 【实体】是否为lombok模型
        strategy.setEntityLombokModel(true);
        // 需要生成的表
        strategy.setInclude(new String[]{"mw_permission"});
        // 排除生成的表
//        strategy.setExclude(new String[]{"oauth_client_details", "persistent_logins", "userconnection", "oauth2_authorized_client", "mw_uc_log"});
        // 自定义实体父类
        strategy.setSuperEntityClass(com.fanmu.muwu.common.coer.extension.model.domain.BaseModel.class);
        // 自定义实体，公共字段
        strategy.setSuperEntityColumns(new String[]{"id", "create_time", "creator", "creator_id", "update_time", "last_operator", "last_operator_id", "is_deleted"});
        // 自定义 mapper 父类
        strategy.setSuperMapperClass("com.fanmu.muwu.common.coer.extension.mapper.BaseMapper");
        // 自定义 service 父类
        strategy.setSuperServiceClass(com.fanmu.muwu.common.coer.extension.service.IService.class);
        // 自定义 service 实现类父类
        strategy.setSuperServiceImplClass(com.fanmu.muwu.common.coer.extension.service.impl.ServiceImpl.class);
        // 自定义 controller 父类
//        strategy.setSuperControllerClass("top.ibase4j.core.base.BaseController");
        // 【实体】是否生成字段常量（默认 false）
        // public static final String ID = "test_id";
//        strategy.setEntityColumnConstant(true);
        // 【实体】是否为构建者模型（默认 false）
        // public User setName(String name) {this.name = name; return this;}
        // strategy.setEntityBuliderModel(true);
        // 逻辑删除属性名称
        strategy.setLogicDeleteFieldName("is_deleted");
        strategy.setRestControllerStyle(true);
        // 是否生成实体时，生成字段注解
        strategy.setEntityTableFieldAnnotationEnable(true);
        mpg.setStrategy(strategy);
        // 配置项目路径
        File file = new File(Generator.class.getProtectionDomain().getCodeSource().getLocation().getFile());
        String path = file.getParentFile().getParent() + "\\src\\main\\";
        // 全局模块名称
        String module = "user";
        // 注入自定义配置，可以在 VM 中使用 cfg.abc 设置的值
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public Map<String, Object> prepareObjectMap(Map<String, Object> objectMap) {
                String tableName = (String) objectMap.get("entity");
                String first = tableName.substring(0, 1).toLowerCase();
                String firstLowerTableName = first + tableName.substring(1);
                String lowerTableName = tableName.toLowerCase();
                objectMap.put("tableName", tableName);
                objectMap.put("firstLowerTableName", firstLowerTableName);
                objectMap.put("lowerTableName", lowerTableName);
                objectMap.put("module", module);
                String moduleFirst = module.substring(0, 1).toUpperCase();
                objectMap.put("firstUpperModule", moduleFirst + module.substring(1));
                return super.prepareObjectMap(objectMap);
            }

            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("rpcService", true);
                map.put("module", module);
                map.put("rpcServiceImplClassPackage", "com.fanmu.muwu.service.provider." + module + ".api.service");
                map.put("dtoPackage", "com.fanmu.muwu.service.provider." + module + ".api.model.dto");
                map.put("queryPackage", "com.fanmu.muwu.service.provider." + module + ".api.model.query");
                map.put("controllerPackage", "com.fanmu.muwu.web.api.controller." + module);
                map.put("rpcServicePackage", "com.fanmu.muwu.service.provider." + module + ".rpc");
                this.setMap(map);
                this.setFileCreate(new IFileCreate() {
                    @Override
                    public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                        if (FileType.OTHER == fileType) {
                            if (filePath.contains("Relation") && (filePath.endsWith("RpcApi.java") || filePath.endsWith("Rpc.java") || filePath.endsWith("Info.java") || filePath.contains("Query") || filePath.endsWith("Controller.java"))) {
                                return false;
                            }
                        }
                        // 全局判断【默认】
                        File file = new File(filePath);
                        boolean exist = file.exists();
                        if (!exist) {
                            file.getParentFile().mkdirs();
                        }
                        return !exist || configBuilder.getGlobalConfig().isFileOverride();
                    }
                });

                String basisPath = System.getProperty("user.dir") + "\\muwu-service-provider-api\\muwu-service-provider-" + module + "-api\\src\\main\\java\\com\\fanmu\\muwu\\service\\provider\\" + module + "\\api\\";
                List<FileOutConfig> fileOutConfigList = new ArrayList<>();
                FileOutConfig rpcServiceApi = new FileOutConfig("templates\\rpcServiceApi.java.vm") {
                    @Override
                    public String outputFile(TableInfo tableInfo) {
                        return basisPath + "service\\" + tableInfo.getEntityName() + "RpcApi.java";
                    }
                };
                FileOutConfig entityInfo = new FileOutConfig("templates\\entityInfo.java.vm") {
                    @Override
                    public String outputFile(TableInfo tableInfo) {
//                        String tableName = tableInfo.getEntityName().substring(module.length());
                        String tableName = tableInfo.getEntityName();
                        String path = basisPath + "model\\dto\\" + tableName.toLowerCase();
                        File file = new File(path);
                        if (!file.exists()) {
                            if (!tableName.contains("Relation")) {
                                file.mkdirs();
                            }
                        }
                        return path + "\\" + tableName + "Info.java";
                    }
                };
                FileOutConfig queryEntity = new FileOutConfig("templates\\queryEntity.java.vm") {
                    @Override
                    public String outputFile(TableInfo tableInfo) {
//                        String tableName = tableInfo.getEntityName().substring(module.length());
                        String tableName = tableInfo.getEntityName();
                        String path = basisPath + "model\\query\\" + tableName.toLowerCase();
                        File file = new File(path);
                        if (!file.exists()) {
                            if (!tableName.contains("Relation")) {
                                file.mkdirs();
                            }
                        }
                        return path + "\\Query" + tableName + ".java";
                    }
                };
                FileOutConfig controller = new FileOutConfig("templates\\controller.java.vm") {
                    @Override
                    public String outputFile(TableInfo tableInfo) {
                        String basisPath = System.getProperty("user.dir") + "\\muwu-web\\muwu-web-api\\src\\main\\java\\com\\fanmu\\muwu\\web\\api\\controller\\" + module + "\\";
                        return basisPath + tableInfo.getEntityName() + "Controller.java";
                    }
                };

                FileOutConfig vue = new FileOutConfig("templates\\vue.java.vm") {
                    @Override
                    public String outputFile(TableInfo tableInfo) {
                        String basisPath = path + "resources\\vue" + "\\";
                        return basisPath + tableInfo.getEntityName() + "index.vue";
                    }
                };

                FileOutConfig vueApi = new FileOutConfig("templates\\vueApi.java.vm") {
                    @Override
                    public String outputFile(TableInfo tableInfo) {
                        String basisPath = path + "resources\\vue" + "\\";
                        return basisPath + tableInfo.getEntityName() + "api.js";
                    }
                };

//                fileOutConfigList.add(rpcServiceApi);
//                fileOutConfigList.add(entityInfo);
//                fileOutConfigList.add(queryEntity);
//                fileOutConfigList.add(controller);
//                fileOutConfigList.add(vue);
//                fileOutConfigList.add(vueApi);

                this.setFileOutConfigList(fileOutConfigList);
            }
        };
        mpg.setCfg(cfg);
        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.fanmu.muwu.service.provider." + module);
        pc.setEntity("model.domain");
        pc.setMapper("mapper");
        pc.setXml("mapper.xml");
        pc.setService("service");
        pc.setServiceImpl("service.impl");
        pc.setController("controller");
        // 配置项目路径
        Map<String, String> pathInfo = new HashMap<>(5);
        pathInfo.put("entity_path", path + "java\\com\\fanmu\\muwu\\service\\provider\\" + module + "\\model\\domain");
        pathInfo.put("xml_path", path + "resources\\mapper");
//        pathInfo.put("mapper_path", path + "java\\com\\fanmu\\muwu\\service\\provider\\" + module + "\\mapper");
//        pathInfo.put("service_path", path + "java\\com\\fanmu\\muwu\\service\\provider\\" + module + "\\service");
//        pathInfo.put("service_impl_path", path + "java\\com\\fanmu\\muwu\\service\\provider\\" + module + "\\service\\impl");
        pc.setPathInfo(pathInfo);
        mpg.setPackageInfo(pc);
        // 放置自己项目的 src/main/resources/template 目录下, 默认名称一下可以不配置，也可以自定义模板名称
        TemplateConfig tc = new TemplateConfig();
//        tc.setEntity("tpl/entity.java.vm");
//        tc.setMapper("tpl/mapper.java.vm");
//        tc.setXml("tpl/mapper.xml.vm");
//        tc.setService("tpl/iservice.java.vm");
//        tc.setServiceImpl("tpl/serviceImpl.java.vm");
//        tc.setController("tpl/controller.java.vm");
        tc.setService("templates\\service.java.vm");
        tc.setServiceImpl("templates\\serviceImpl.java.vm");
        mpg.setTemplate(tc);
        // 执行生成
        mpg.execute();
    }
}
