package com.ustc.software.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author li
 * @Title:
 * @Description:swagger的配置类，Swagger实例Bean是Docket,创建docket即可。
 * @date 2020/8/27 10:33
 */
@Configuration
public class SwaggerConfig2 {
    /**
     * 通过.select()方法，去配置扫描接口,RequestHandlerSelectors配置如何扫描接口
     * @return docket
     */
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(createApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ustc.software.controller"))
                .build();
    }

    /**
     * @return apiInfo bean用于创建API基本信息 【作者、简介、版本、host、服务URL】
     */
    public ApiInfo createApiInfo(){
        return new ApiInfoBuilder()
                .version("1.0")
                .title("community")
                .description("for test swagger2")
                .build();
    }
}
