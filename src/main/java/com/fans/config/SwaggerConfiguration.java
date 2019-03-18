package com.fans.config;

import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @ClassName SwaggerConfiguration
 * @Description: Swagger配置
 * @Author fan
 * @Date 2018-12-21 21:07
 * @Version 1.0
 **/
@Component
@Configuration
@EnableSwagger2
@Data
@ConfigurationProperties(prefix = "swagger")
@ConditionalOnExpression("${swagger.enable}")
@Slf4j
public class SwaggerConfiguration {
    /**
     * 当前文档的标题
     */
    private String title = "Kapok RestFul System";
    /**
     * 当前文档的详细描述
     */
    private String description = "poweredByBy-Health";
    /**
     * controller接口所在的包
     */
    private String basePackage = "com.fans.controller";
    /**
     * 当前文档的版本
     */
    private String version = "1.0";
    /**
     * 服务接口源码地址
     */
    private String serviceUrl = "https://github.com";

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //加了ApiOperation注解的类，生成接口文档
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                //加了RestController注解的类，生成接口文档
                //.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .termsOfServiceUrl(serviceUrl)
                .version(version)
                .build();
    }


}
