/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.common.web.config;

import com.fanmu.muwu.common.config.properties.MuWuProperties;
import com.fanmu.muwu.common.config.properties.SwaggerProperties;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The class Swagger configuration.
 *
 */
@EnableSwagger2
public class SwaggerConfiguration {
    @Resource
    private MuWuProperties muWuProperties;

    /**
     * Reservation api docket.
     *
     * @return the docket
     */
    @Bean
    public Docket createRestApi() {
        //每次都需手动输入header信息
/*		ParameterBuilder pb = new ParameterBuilder();
		List<Parameter> pars = new ArrayList();
		pb.name("Authorization").description("user access_token")
				.modelRef(new ModelRef("string")).parameterType("header")
				.required(true).build(); //header中的ticket参数非必填，传空也可以
		pars.add(pb.build());    //根据每个方法名也知道当前方法在设置什么参数*/
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())

                .build()
                //配置鉴权信息
//                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
//				.globalOperationParameters(pars)
                .enable(true);
    }

    private ApiInfo apiInfo() {
        SwaggerProperties swagger = muWuProperties.getSwagger();
        return new ApiInfoBuilder()
                .title(swagger.getTitle())
                .description(swagger.getDescription())
                .version(swagger.getVersion())
                .license(swagger.getLicense())
                .licenseUrl(swagger.getLicenseUrl())
                .contact(new Contact(swagger.getContactName(), swagger.getContactUrl(), swagger.getContactEmail()))
                .build();
    }

    private List<ApiKey> securitySchemes() {
        return new ArrayList<>(Collections.singleton(new ApiKey("Authorization", "Authorization", "header")));
    }

    private List<SecurityContext> securityContexts() {
        return new ArrayList<>(
                Collections.singleton(SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(PathSelectors.regex("^(?!auth).*$"))
                        .build())
        );
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return new ArrayList<>(Collections.singleton(new SecurityReference("Authorization", authorizationScopes)));
    }

}
