package com.mysl.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger 3 配置类
 * @author Ivan Su
 * @date 2022/7/30
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig {

    @Value("${springfox.documentation.swagger-ui.enabled}")
    private boolean enabled;

    @Bean
    public Docket createRestApi() {
        //返回文档摘要信息
        return new Docket(DocumentationType.OAS_30)
                // 是否开启 Swagger
                .enable(enabled)
                // 配置项目基本信息
                .apiInfo(apiInfo())
                // 设置项目组名
                .groupName("App")
                // 选择那些路径和api会生成document
                .select()
                // 对所有api进行监控
                // .apis(RequestHandlerSelectors.any())
                // 如果需要指定对某个包的接口进行监控，则可以配置如下
                .apis(RequestHandlerSelectors.basePackage("com.mysl.api.controller.app"))
                // 对所有路径进行监控
                .paths(PathSelectors.any())
                // 忽略以"/error"开头的路径,可以防止显示如404错误接口
                .paths(PathSelectors.regex("/error.*").negate())
                // 忽略以"/actuator"开头的路径
                .paths(PathSelectors.regex("/actuator.*").negate())
                .build();
    }

    /**
     * 接口信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SHILI-API 接口文档")
                .version("1.0.0")
                .build();
    }

}
