package com.mysl.api.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mysl.api.common.lang.Result;
import com.mysl.api.lib.AesFile;
import com.mysl.api.lib.Location;
import com.mysl.api.lib.Permission;
import com.mysl.api.service.Upload;
import com.mysl.api.entity.MediaRead;
import com.mysl.api.mapper.MediaReadMapper;
import com.mysl.api.service.MediaReadService;
import com.mysl.api.service.UsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sql/mediaRead")
public class MediaReadController {
  @Autowired
  UsersService usersService;

  @Autowired
  MediaReadService mediaReadService;

  @Autowired
  Upload uploadService;

  @Resource
  MediaReadMapper mediaReadMapper;

  @PostMapping("create") // 不在此创建
  public Object create(HttpServletRequest request) throws IOException {
    return uploadService.index(request);
  }

  @PostMapping("update")
  public Object update(HttpServletRequest request) throws IOException {
    JSONObject p = AesFile.request2json(request);
    Permission per = usersService.getPerByJson(p);
    Long id = p.getLong("id");
    Long mid = p.getLong("media");
    Long uid = p.getLong("user");
    JSONObject jsLocation = p.getJSONObject("location");
    Double latitude = jsLocation.getDouble("latitude");
    Double longitude = jsLocation.getDouble("longitude");
    if (latitude == null || longitude == null) {
      return Result.ret(301, "location非法");
    }
    Location location = new Location();
    location.setLatitude(latitude);
    location.setLongitude(longitude);
    try {
      String t = "http://apis.map.qq.com/ws/geocoder/v1?key=DRXBZ-EMXWX-Q6E4A-ZJIO3-XJUJF-NUBJH&output=json&location="
          + latitude + "," + longitude;
      byte[] d = AesFile.httpGet(t);
      String r = new String(d);
      r = r.substring(r.indexOf("{"), r.lastIndexOf("}") + 1);
      JSONObject j = JSONObject.parseObject(r);
      location.setAddress(j.getJSONObject("result").getString("address"));
    } catch (Exception e) {
      location.setAddress("");
    }
    do {
      QueryWrapper<MediaRead> qmr = new QueryWrapper<>();
      if (per.node(1, 1 << (0 + 1))) {
        long place = AesFile.Placeholder(per.group);
        if (uid != null) {
          Permission up = usersService.getPerById(uid);
          if (id == null || (up.group & place) != place) {
            break;
          }
        }
      } else {
        if (uid == null) {
          uid = per.id;
        } else if (uid != per.id) {
          break;
        }
        qmr.eq("location", "{}");
      }
      MediaRead mr = new MediaRead();
      mr.setEndTime(AesFile.time());
      mr.setLocation(JSONObject.toJSONString(location));

      if (id != null) {
        qmr.eq("id", id);
      }
      if (mid != null) {
        qmr.eq("media", mid);
      }
      if (uid != null) {
        qmr.eq("user", uid);
      }
      return Result.ret((Object) mediaReadMapper.update(mr, qmr));
    } while (false);
    return Result.ret(101, "没有权限");
  }

  @PostMapping("read")
  public Object read(HttpServletRequest request) throws IOException {
    JSONObject p = AesFile.request2json(request);
    return Result.ret(mediaReadService.getObjByJson(p));
  }

  @PostMapping("delete") // 禁止删除
  public Object delete(HttpServletRequest request) throws IOException {
    // JSONObject p = AesFile.request2json(request);
    // Permission per = usersService.getPerByJson(p);
    // long place = AesFile.Placeholder(per.group);
    // Long id = p.getLong("id");
    // if (id != 0 && per.node(1, 1 << (0 + 3))) {
    // QueryWrapper<MediaRead> q = new QueryWrapper<>();
    // q.eq("id", id);
    // q.apply("`group`&" + place + "=" + per.group);
    // return Result.ret((Object) mediaReadMapper.delete(q));
    // }
    return Result.ret(101, "没有权限");
  }
}