package com.demo.modules_campus.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName SpringMvcConfig
 * @Description TODO
 * @Date 2023/3/22 10:04
 * @Created by admin
 */
@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {
  @Value("${upload.file.location}")
  private String uploadPath;

  /***
   * 配置静态资源映射，解决上传文件后需要重新编译才能访问的问题
   * @param registry
   */

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    if (!registry.hasMappingForPattern("/upload/**")) {
      registry.addResourceHandler("/upload/**").addResourceLocations("file:" + uploadPath);
    }
  }
}
