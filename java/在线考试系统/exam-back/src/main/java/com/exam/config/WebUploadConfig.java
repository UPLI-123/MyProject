package com.exam.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @projectName: exam
 * @package: com.exam.config
 * @className: WebUploadConfig
 * @author: LCH
 * @description: 上传文件 配置
 * @date: 2022/4/17 15:40
 * @version: 1.0
 */
@Configuration
public class WebUploadConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("upload/**").addResourceLocations("file:D:\\image\\");
    }
}
