package com.mysl.api.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mysl.api.common.lang.Result;
import com.mysl.api.lib.AesFile;
import com.mysl.api.lib.Permission;
import com.mysl.api.service.Upload;
import com.mysl.api.entity.HaveMedia;
import com.mysl.api.entity.MediaV1;
import com.mysl.api.mapper.HaveMediaMapper;
import com.mysl.api.mapper.MediaV1Mapper;
import com.mysl.api.service.MediaV1Service;
import com.mysl.api.service.UsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sql/media")
public class MediaController {
  @Autowired
  UsersService usersService;

  @Autowired
  MediaV1Service mediaV1Service;

  @Resource
  MediaV1Mapper mediaV1Mapper;

  @Resource
  HaveMediaMapper haveMediaMapper;

  @Autowired
  Upload upload;

  @PostMapping("create")
  public Object create(HttpServletRequest request) {
    return upload.index(request);
  }

  @PostMapping("update")
  public Object update(HttpServletRequest request) {
    JSONObject p = AesFile.request2json(request);
    Permission per = usersService.getPerByJson(p);
    Long id = p.getLong("id");
    if (id != null && per.group == 0) {
      MediaV1 data = new MediaV1();
      data.setId(id);
      data.setState(p.getInteger("state"));
      data.setSort(p.getInteger("sort"));
      data.setTitle(p.getString("title"));
      data.setImg(p.getString("img"));
      data.setSimple(p.getString("simple"));
      return mediaV1Service.updateById(data) ? Result.ret(data) : Result.ret(501, "服务器错误,请重试");
    }
    return Result.ret(101, "没有权限");
  }

  @PostMapping("read")
  public Object read(HttpServletRequest request) {
    JSONObject p = AesFile.request2json(request);
    Permission per = usersService.getPerByJson(p);
    if (per.group == 0) {
      // long place = AesFile.Placeholder(per.group);
      long page = p.getLongValue("page");
      long size = p.getLongValue("size");
      Long id = p.getLong("id");
      String type = p.getString("type");
      Integer state = p.getInteger("state");
      String title = p.getString("title");
      String simple = p.getString("simple");
      if (size < 10) {
        size = 10;
      } else if (size > 100) {
        size = 100;
      }
      if (page < 0) {
        page = 0;
      }
      QueryWrapper<MediaV1> q = new QueryWrapper<>();
      if (id != 0) {
        q.eq("id", id);
      }
      if (type != null) {
        q.eq("type", type);
      }
      if (state != null) {
        q.eq("state", state);
      }
      if (title != null) {
        q.like("title", title);
      }
      if (simple != null) {
        q.like("simple", simple);
      }
      return Result.ret(page == 0 ? mediaV1Mapper.selectList(q) : mediaV1Mapper.selectPage(new Page<MediaV1>(page, size), q));
    }
    return Result.ret(101, "没有权限");
  }

  @PostMapping("delete")
  public Object delete(HttpServletRequest request) {
    JSONObject p = AesFile.request2json(request);
    Permission per = usersService.getPerByJson(p);
    Long id = p.getLong("id");
    if (id != null && per.node(0, 1 << (4 + 3))) {
      QueryWrapper<MediaV1> q = new QueryWrapper<>();
      q.eq("id", id);
      int r = mediaV1Mapper.delete(q);
      if (r > 0) {
        String path = "crypto/" + id;
        if (!AesFile.DeleteFolder(path)) {
          AesFile.DeleteFolder(path + ".aac");
        }
        QueryWrapper<HaveMedia> hmq = new QueryWrapper<>();
        hmq.eq("media", id);
        haveMediaMapper.delete(hmq);
      }
      return Result.ret((Object) r);
    }
    return Result.ret(101, "没有权限");
  }
}