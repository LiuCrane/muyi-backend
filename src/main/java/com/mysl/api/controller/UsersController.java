package com.mysl.api.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mysl.api.common.lang.Result;
import com.mysl.api.lib.AesFile;
import com.mysl.api.lib.Permission;
import com.mysl.api.entity.Users;
import com.mysl.api.mapper.UsersMapper;
import com.mysl.api.service.UsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sql/users")
public class UsersController {
  @Autowired
  UsersService usersService;

  @Resource
  UsersMapper usersMapper;

  @PostMapping("create")
  public Object create(HttpServletRequest request) throws IOException {
    JSONObject p = AesFile.request2json(request);
    Permission per = usersService.getPerByJson(p);
    if (per.node(0, 1 << (0 + 0))) {
      long place = AesFile.Placeholder(per.group);
      Users data = new Users();
      long group = p.getLongValue("group");
      if ((place & group) == per.group) {
        data.setGroup(group);
        String pu = p.getJSONObject("infoPrivate").toJSONString();
        String pr = p.getJSONObject("infoPublic").toJSONString();
        if (pu == null || pu.length() < 2) {
          pu = "{}";
        }
        if (pr == null || pr.length() < 2) {
          pr = "{}";
        }
        data.setUsername(p.getString("username"));
        data.setPassword(p.getString("password"));
        data.setInfoPrivate(pr);
        data.setInfoPublic(pu);
        data.setCreateTime(AesFile.time());
        data.setCmr("");
        data.setWxid(IdWorker.get32UUID());
        return usersService.save(data) ? Result.ret(data) : Result.ret(501, "参数不正确");
      }
    }
    return Result.ret(101, "没有权限");
  }

  @PostMapping("update")
  public Object update(HttpServletRequest request) throws IOException {
    JSONObject p = AesFile.request2json(request);
    Permission per = usersService.getPerByJson(p);
    Long id = p.getLong("id");
    if (id == null) {
      id = per.id;
    }
    if (id == per.id || per.node(0, 1 << (0 + 1))) {
      long place = AesFile.Placeholder(per.group);
      Long group = p.getLong("group");
      if (group != null && ((group & place) != per.group)) {
        return Result.ret(101, "没有权限");
      }
      Users user = new Users();
      user.setId(id);
      Permission up = usersService.getPerById(id);
      if ((place & up.group) == per.group) {
        user.setGroup(group);
        user.setPassword(p.getString("password"));
        try {
          String pu = p.getJSONObject("infoPublic").toJSONString();
          if (pu != null && pu.length() > 2) {
            user.setInfoPublic(pu);
          }
        } catch (Exception e) {
        }
        try {
          String pr = p.getJSONObject("infoPrivate").toJSONString();
          if (pr != null && pr.length() > 2) {
            user.setInfoPrivate(pr);
          }
        } catch (Exception e) {
        }
        user.setState(p.getInteger("state"));
        user.setPermission(p.getBytes("permission"));
        user.setWxid(p.getString("wxid"));
        if (usersService.updateById(user)) {
          p = new JSONObject();
          p.put("token", usersService.getToken(user));
          user = usersService.getById(id);
          user.setPassword(null);
          p.put("me", user);
          return Result.ret(p);
        }
      }
    }
    return Result.ret(101, "没有权限");
  }

  @PostMapping("read")
  public Object read(HttpServletRequest request) throws IOException {
    JSONObject p = AesFile.request2json(request);
    Permission per = usersService.getPerByJson(p);
    if (per.node(0, 1 << (0 + 2))) {
      long place = AesFile.Placeholder(per.group);
      long page = p.getLongValue("page");
      long size = p.getLongValue("size");
      Long id = p.getLong("id");
      Long gid = p.getLong("group");
      if (size < 10) {
        size = 10;
      } else if (size > 100) {
        size = 100;
      }
      if (page < 0) {
        page = 0;
      }
      QueryWrapper<Users> q = new QueryWrapper<>();
      if (id != null) {
        q.eq("id", id);
      }
      if (gid != null) {
        q.eq("group", gid);
      }
      q.apply("`group`&" + place + "=" + per.group);
      return Result.ret(page == 0 ? usersMapper.selectList(q) : usersMapper.selectPage(new Page<Users>(page, size), q));
    }
    return Result.ret(101, "没有权限");
  }

  @PostMapping("delete")
  public Object delete(HttpServletRequest request) throws IOException {
    JSONObject p = AesFile.request2json(request);
    Permission per = usersService.getPerByJson(p);
    long place = AesFile.Placeholder(per.group);
    Long id = p.getLong("id");
    if (id != null && id != 0 && per.node(0, 1 << (0 + 3))) {
      QueryWrapper<Users> q = new QueryWrapper<>();
      q.eq("id", id);
      q.apply("`group`&" + place + "=" + per.group);
      return Result.ret((Object) usersMapper.delete(q));
    }
    return Result.ret(101, "没有权限");
  }
}