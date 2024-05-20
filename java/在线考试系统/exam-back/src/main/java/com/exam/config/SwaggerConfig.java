package com.exam.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.w3c.dom.DocumentType;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket docket(){

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("lch")
                .apiInfo(getApiInfo())
                .enable(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.exam.controller"))
                .paths(PathSelectors.any())
                .build() ;
    }

    private ApiInfo getApiInfo(){
        Contact DEFAULT_CONTACT = new Contact("李长昊","https://blog.csdn.net/qq_43345339","1789412739@qq.com") ;
        return new ApiInfo("在线考试系统","前后端分离项目--毕业设计"
        ,"1.0","",DEFAULT_CONTACT,"Apache 2.0"
        ,"http://www.apache.org/licenses/LICENSE-2.0"
                ,new ArrayList<>()) ;
    }


}
