package com.mysl.api.config;

import com.alibaba.fastjson.JSONObject;
import com.mysl.api.config.Open.Open;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.Data;

@Data
@Component
@EnableConfigurationProperties
@EnableTransactionManagement
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "mysl")
public class MyConfig {

  @Value("${spring.datasource.username}")
  public String sqlUsername;

  @Value("${spring.datasource.password}")
  public String sqlPassword;

  @NestedConfigurationProperty
  public Open open;

  @Value("${server.port}")
  public int port;

  @Override
  public String toString() {
    return JSONObject.toJSONString(this);
  }

}
