package com.mysl.api.config.Open;

import com.alibaba.fastjson.JSONObject;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@EnableConfigurationProperties
// @EnableTransactionManagement
public class Open {
  @NestedConfigurationProperty
  public Wx wx;

  @Override
  public String toString() {
    return JSONObject.toJSONString(this);
  }

}
