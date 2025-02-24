package com.mapoh.ppg.config;

/**
 * @author mabohv
 * @date 2025/2/16 23:07
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                // 设置你要扫描的 Controller 包
                .apis(RequestHandlerSelectors.basePackage("com.mapoh.ppg.controller.AuthController"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(new ApiInfoBuilder().title("API Documentation")
                        .description("API Documentation for Spring Boot")
                        .version("1.0.0")
                        .build());
    }
}