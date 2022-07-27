package com.mysl.api.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mysl.api.common.lang.Result;
import com.mysl.api.lib.AesFile;
import com.mysl.api.lib.Permission;
import com.mysl.api.entity.Group;
import com.mysl.api.service.GroupService;
import com.mysl.api.service.UsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 权限组 前端控制器
 * </p>
 *
 * @author mac-xiang
 * @since 2021-05-07
 */
@RestController
@RequestMapping("/sql/group")
public class GroupController {
  @Autowired
  GroupService groupService;

  @Autowired
  UsersService usersService;

  @PostMapping("create")
  public Object create(HttpServletRequest request) throws IOException {
    JSONObject p = AesFile.request2json(request);
    Permission per = usersService.getPerByJson(p);
    if (per.node(2, 1 << 4)) {
      List<Group> gs = groupService.getByBit(per.group);
      List<Long> ids = new ArrayList<>();
      long id = per.group, t = 0;
      int i = 0;
      while (id != 0) {
        id >>= 8;
        i++;
      }
      long t1 = 0xff << ((i + 1) * 8);
      gs.forEach(g -> {
        if ((g.getId() & t1) == 0) {
          ids.add(g.getId());
        }
      });
      while (t++ <= 255) {
        id = t << i | per.group;
        if (ids.indexOf(id) < 0) {
          Group g = new Group();
          g.setId(id);
          g.setName(p.getString("name"));
          g.setPermission(p.getBytes("permission"));
          return groupService.save(g) ? Result.ret(g) : Result.ret(501, "服务器错误,请重试");
        }
      }
      return Result.ret(601, "层级数据已满");
    }
    return Result.ret(101, "没有权限");
  }

  @PostMapping("update")
  public Object update(HttpServletRequest request) throws IOException {
    JSONObject p = AesFile.request2json(request);
    Permission per = usersService.getPerByJson(p);
    long id = p.getLongValue("id");
    if (id != 0 && per.node(2, 1 << 5)) { // 参数存在id并且拥有修改权限
      long place = AesFile.Placeholder(per.group);
      if ((place & id) == (place & per.group)) { // 判定所修改用户必须与自己同一组
        byte[] up = p.getBytes("permission");
        if (up.length <= per.permission.length) { // 查看参数权限数据是否长度超出
          int i = 0;
          while (i < up.length) { // 遍历权限数据 判定是否权限溢出
            if ((up[i] & per.permission[i]) != up[i]) { // 权限超出
              break;
            }
            i++;
          }
          if (i == up.length) {
            Group g = new Group();
            g.setId(id);
            g.setName(p.getString("name"));
            g.setPermission(up);
            return groupService.updateById(g) ? Result.ret(g) : Result.ret(501, "服务器错误");
          }
        }
      }
    }
    return Result.ret(101, id == 0 ? "超管组不允许修改" : "没有权限");
  }

  @PostMapping("read")
  public Object read(HttpServletRequest request) throws IOException {
    JSONObject p = AesFile.request2json(request);
    Permission per = usersService.getPerByJson(p);
    int page = p.getIntValue("page"), size = p.getIntValue("size");
    if (size < 10) {
      size = 10;
    } else if (size > 100) {
      size = 100;
    }
    if (page < 0) {
      page = 0;
    }
    return Result.ret(
        page == 0 ? groupService.getByBit(per.group) : groupService.getByBit(per.group, new Page<Group>(page, size)));
  }

  @PostMapping("delete")
  public Object delete(HttpServletRequest request) throws IOException {
    JSONObject p = AesFile.request2json(request);
    Permission per = usersService.getPerByJson(p);
    Long id = p.getLong("id");
    if (id != null && id != 0 && per.node(2, 1 << 5)) { // 参数存在id并且拥有修改权限
      long place = AesFile.Placeholder(per.group);
      if ((place & id) == (place & per.group)) { // 判定所修改用户必须与自己同一组
        return Result.ret(groupService.removeById(id));
      }
    }
    return Result.ret(101, id == 0 ? "超管组不允许修改" : "没有权限");
  }
}
