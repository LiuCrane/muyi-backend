package com.mysl.api;

import java.io.IOException;

import com.mysl.api.service.MediaReadService;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ComponentScan(basePackages = {"com.mysl.api" })
@MapperScan("com.mysl.api.mapper")
@SpringBootApplication
public class ApiApplication implements WebMvcConfigurer {

  @Value("${server.port}")
  public int port;

  @Autowired
  MediaReadService mediaReadService;

  /* @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    String path = AesFile.formatFilename("public", true);
    if (path == null) {
      return;
    }
    File fi = new File(path);// 参数为空
    if (!fi.exists() && !fi.mkdirs()) {
      return;
    }

    registry.addResourceHandler("*").addResourceLocations("file:" + path);
    AesFile.clear();
    log.info("端口: " + port);
    log.info("自定义静态资源目录: " + path);

    mediaReadService.upLocationAll();
  } */

  public static void main(String[] args) throws IOException {
    SpringApplication.run(ApiApplication.class, args);
  }

}
