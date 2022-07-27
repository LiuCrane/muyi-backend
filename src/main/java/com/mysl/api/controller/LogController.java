package com.mysl.api.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mysl.api.common.lang.Result;
import com.mysl.api.lib.AesFile;
import com.mysl.api.lib.Permission;
import com.mysl.api.entity.Log;
import com.mysl.api.mapper.LogMapper;
import com.mysl.api.service.LogService;
import com.mysl.api.service.UsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sql/log")
public class LogController {
  @Autowired
  UsersService usersService;

  @Autowired
  LogService logService;

  @Resource
  LogMapper logMapper;

  // #region CU
  // @PostMapping("create")
  // public Object create(HttpServletRequest request) throws IOException {
  //   JSONObject p = AesFile.request2json(request);
  //   Permission per = usersService.getPerByJson(p);
  //   long mid = p.getLongValue("media");
  //   if (mid != 0 && per.node(1, 1 << (0 + 0))) {
  //     long place = AesFile.Placeholder(per.group);
  //     long uid = p.getLongValue("user");
  //     Log data = new Log();
  //     if (uid == 0) {
  //       long group = p.getLongValue("group");
  //       if ((place & group) == per.group) {
  //         data.setMedia(mid);
  //         data.setUser(uid);
  //         data.setGroup(group);
  //         return logService.save(data) ? Result.ret(data) : Result.ret(501, "服务器错误,请重试");
  //       }
  //     } else {
  //       Permission up = usersService.getPerById(uid);
  //       if ((place & up.group) == per.group) {
  //         data.setMedia(mid);
  //         data.setUser(uid);
  //         data.setGroup(up.group);
  //         return logService.save(data) ? Result.ret(data) : Result.ret(501, "服务器错误,请重试");
  //       }
  //     }
  //   }
  //   return Result.ret(101, "没有权限");
  // }

  // @PostMapping("update")
  // public Object update(HttpServletRequest request) throws IOException {
  //   JSONObject p = AesFile.request2json(request);
  //   Permission per = usersService.getPerByJson(p);
  //   long id = p.getLongValue("id");
  //   long mid = p.getLongValue("media");
  //   if (id != 0 && mid != 0 && per.node(1, 1 << (0 + 1))) {
  //     long place = AesFile.Placeholder(per.group);
  //     long uid = p.getLongValue("user");
  //     Log data = new Log();
  //     data.setId(id);
  //     if (uid == 0) {
  //       long group = p.getLongValue("group");
  //       if ((place & group) == per.group) {
  //         data.setMedia(mid);
  //         data.setUser(uid);
  //         data.setGroup(group);
  //         return logService.updateById(data) ? Result.ret(data) : Result.ret(501, "服务器错误,请重试");
  //       }
  //     } else {
  //       Permission up = usersService.getPerById(uid);
  //       if ((place & up.group) == per.group) {
  //         data.setMedia(mid);
  //         data.setUser(uid);
  //         data.setGroup(up.group);
  //         return logService.updateById(data) ? Result.ret(data) : Result.ret(501, "服务器错误,请重试");
  //       }
  //     }
  //   }
  //   return Result.ret(101, "没有权限");
  // }
  // #endregion

  @PostMapping("read")
  public Object read(HttpServletRequest request) throws IOException {
    JSONObject p = AesFile.request2json(request);
    Permission per = usersService.getPerByJson(p);
    if (per.node(1, 1 << (0 + 2))) {
      long place = AesFile.Placeholder(per.group);
      long page = p.getLongValue("page");
      long size = p.getLongValue("size");
      Long id = p.getLong("id");
      Long mid = p.getLong("media");
      Long uid = p.getLong("user");
      Long gid = p.getLong("group");
      if (size < 10) {
        size = 10;
      } else if (size > 100) {
        size = 100;
      }
      if (page < 0) {
        page = 0;
      }
      QueryWrapper<Log> q = new QueryWrapper<>();
      if (id != 0) {
        q.eq("id", id);
      }
      if (mid != 0) {
        q.eq("media", mid);
      }
      if (uid != 0) {
        q.eq("user", uid);
      }
      if (gid != 0) {
        q.eq("group", gid);
      }
      q.apply("`group`&" + place + "=" + per.group);
      return Result.ret(page == 0 ? logMapper.selectList(q) : logMapper.selectPage(new Page<Log>(page, size), q));
    }
    return Result.ret(101, "没有权限");
  }

  @PostMapping("delete")
  public Object delete(HttpServletRequest request) throws IOException {
    JSONObject p = AesFile.request2json(request);
    Permission per = usersService.getPerByJson(p);
    long place = AesFile.Placeholder(per.group);
    Long id = p.getLong("id");
    if (id != 0 && per.node(1, 1 << (0 + 3))) {
      QueryWrapper<Log> q = new QueryWrapper<>();
      q.eq("id", id);
      q.apply("`group`&" + place + "=" + per.group);
      return Result.ret((Object) logMapper.delete(q));
    }
    return Result.ret(101, "没有权限");
  }
}