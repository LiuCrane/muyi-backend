package com.mysl.api.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.mysl.api.common.lang.Result;
import com.mysl.api.lib.AesFile;
import com.mysl.api.lib.HMU;
import com.mysl.api.lib.Permission;
import com.mysl.api.service.GetGD;
import com.mysl.api.entity.HaveMedia;
import com.mysl.api.mapper.HaveMediaMapper;
import com.mysl.api.service.HaveMediaService;
import com.mysl.api.service.UsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sql/haveMedia")
public class HaveMediaController {
  @Autowired
  UsersService usersService;

  @Autowired
  HaveMediaService haveMediaService;

  @Resource
  HaveMediaMapper haveMediaMapper;

  @Autowired
  GetGD getGD;

  @PostMapping("create")
  public Object create(HttpServletRequest request) throws IOException {
    JSONObject p = AesFile.request2json(request);
    Permission per = usersService.getPerByJson(p);
    long mid = p.getLongValue("media");
    if (mid != 0 && per.node(1, 1 << (4 + 0))) {
      long place = AesFile.Placeholder(per.group);
      long uid = p.getLongValue("user");
      HaveMedia hm = new HaveMedia();
      hm.setRead(p.getInteger("read"));
      if (uid == 0) {
        long group = p.getLongValue("group");
        if ((place & group) == per.group) {
          hm.setId(IdWorker.getId());
          hm.setMedia(mid);
          hm.setUser(uid);
          hm.setGroup(group);
          if (haveMediaService.save(hm)) {
            QueryWrapper<HaveMedia> q = new QueryWrapper<>();
            q.eq("`have_media`.id", hm.getId());
            List<HMU> t = haveMediaMapper.getHMU(q);
            return Result.ret(t.get(0));
          }
          return Result.ret(501, "服务器错误,请重试");
        }
      } else {
        Permission up = usersService.getPerById(uid);
        if ((place & up.group) == per.group) {
          hm.setId(IdWorker.getId());
          hm.setMedia(mid);
          hm.setUser(uid);
          hm.setGroup(up.group);
          if (haveMediaService.save(hm)) {
            QueryWrapper<HaveMedia> q = new QueryWrapper<>();
            q.eq("`have_media`.id", hm.getId());
            List<HMU> t = haveMediaMapper.getHMU(q);
            return Result.ret(t.get(0));
          }
          return Result.ret(501, "服务器错误,请重试");
        }
      }
    }
    return Result.ret(101, "没有权限");
  }

  @PostMapping("update")
  public Object update(HttpServletRequest request) throws IOException {
    JSONObject p = AesFile.request2json(request);
    Permission per = usersService.getPerByJson(p);
    Long id = p.getLong("id");
    Long mid = p.getLong("media");
    if (id != null && per.node(1, 1 << (4 + 0))) {
      long place = AesFile.Placeholder(per.group);
      Long uid = p.getLong("user");
      HaveMedia hm = new HaveMedia();
      hm.setId(id);
      hm.setRead(p.getInteger("read"));
      if (uid == null || uid == 0) {
        Long group = p.getLong("group");
        if (group == null || ((place & group) == per.group)) {
          hm.setMedia(mid);
          hm.setUser(uid);
          hm.setGroup(group);
          return haveMediaService.updateById(hm) ? Result.ret(hm) : Result.ret(501, "服务器错误,请重试");
        }
      } else {
        Permission up = usersService.getPerById(uid);
        if ((place & up.group) == per.group) {
          hm.setMedia(mid);
          hm.setUser(uid);
          hm.setGroup(up.group);
          return haveMediaService.updateById(hm) ? Result.ret(hm) : Result.ret(501, "服务器错误,请重试");
        }
      }
    }
    return Result.ret(101, "没有权限");
  }

  @PostMapping("read")
  public Object read(HttpServletRequest request) {
    JSONObject p = AesFile.request2json(request);
    return Result.ret(haveMediaService.getObjByJson(p));
    // return null;
  }

  @GetMapping("read")
  public Object read2(HttpServletRequest request) throws IOException {
    JSONObject p = AesFile.request2json(request);
    return Result.ret(haveMediaService.getObjByJson(p));
  }

  @PostMapping("delete")
  public Object delete(HttpServletRequest request) throws IOException {
    JSONObject p = AesFile.request2json(request);
    Permission per = usersService.getPerByJson(p);
    long place = AesFile.Placeholder(per.group);
    Long id = p.getLong("id");
    Long media = p.getLong("media");

    if (per.node(1, 1 << (4 + 3))) {
      QueryWrapper<HaveMedia> q = new QueryWrapper<>();
      if (id != null) {
        q.eq("id", id);
      }
      if (media != null) {
        q.eq("`media`", media);
      }
      if (id != null || media != null) {
        q.apply("`group`&" + place + "=" + per.group);
        return Result.ret((Object) haveMediaMapper.delete(q));
      }
    }
    return Result.ret(101, "没有权限");
  }
}
