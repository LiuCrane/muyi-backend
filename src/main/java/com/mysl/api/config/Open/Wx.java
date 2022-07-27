package com.mysl.api.config.Open;

import com.alibaba.fastjson.JSONObject;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@EnableConfigurationProperties
// @EnableTransactionManagement
public class Wx {
  public String xcxId;
  public String xcxSecret;

  public String gzhId;
  public String gzhSecret;
  @Override
  public String toString() {
    return JSONObject.toJSONString(this);
  }

}
