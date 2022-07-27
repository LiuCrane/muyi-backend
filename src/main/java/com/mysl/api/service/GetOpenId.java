package com.mysl.api.service;

import com.alibaba.fastjson.JSONObject;
import com.mysl.api.config.MyConfig;
import com.mysl.api.lib.AesFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetOpenId {
  @Autowired
  MyConfig config;

  public String wx(String code) {
    String url = "https://api.weixin.qq.com/sns/jscode2session";
    url += "?appid=" + config.open.wx.xcxId;// 自己的appid
    url += "&secret=" + config.open.wx.xcxSecret;// 自己的appSecret
    url += "&js_code=" + code;
    url += "&grant_type=authorization_code";
    url += "&connect_redirect=1";
    try {
      return JSONObject.parseObject(new String(AesFile.httpGet(url), "utf8")).getString("openid");
    } catch (Exception e) {
      throw new RuntimeException("openid获取失败");
    }

    // JSONObject jo = JSONObject.parseObject(res);
    // String openid = jo.getString("openid");
    // log.info("openid" + openid);
    // return openid;
  }
}
