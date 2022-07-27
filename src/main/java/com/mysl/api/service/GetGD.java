package com.mysl.api.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mysl.api.lib.AesFile;
import com.mysl.api.lib.GlobalData;
import com.mysl.api.lib.Permission;
import com.mysl.api.entity.Group;
import com.mysl.api.entity.Users2;
import com.mysl.api.mapper.AddressMapper;
import com.mysl.api.mapper.GroupMapper;
import com.mysl.api.mapper.HaveMediaMapper;
import com.mysl.api.mapper.MediaMapper;
import com.mysl.api.mapper.Users2Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetGD {
  @Autowired
  AddressMapper addressMapper;

  @Autowired
  GroupMapper groupMapper;

  @Autowired
  MediaMapper mediaMapper;

  @Autowired
  Users2Mapper users2Mapper;

  @Autowired
  HaveMediaMapper haveMediaMapper;

  @Autowired
  UsersService usersService;

  public GlobalData index(Permission per) {
    GlobalData gd = new GlobalData();
    long place = AesFile.Placeholder(per.group);

    gd.setAddress(addressMapper.selectList(null));
    if (per.node(2, 1 << 4)) { // 权限组读m取
      gd.setGroup(groupMapper.selectList(null));
      if (per.group == 0) {
        Group g = new Group();
        g.setId(0l);
        g.setName("超级管理员");
        g.setPermission(per.permission);
        gd.group.add(0, g);
      }
    }
    if (per.node(0, 1 << 4)) { // 媒体读取
      gd.setMedia(mediaMapper.selectList(null));
    }
    QueryWrapper<Users2> uq = new QueryWrapper<>();
    if (per.node(0, 0)) {
      uq.select("id", "info_public", "info_private");
    } else {
      uq.select("id", "info_public");

    }
    uq.apply("(`group`&" + place + ")=" + per.group);
    gd.setUsers(users2Mapper.selectList(uq));
    gd.setMe(users2Mapper.getById(per.id));

    return gd;
  }

  public Object index(JSONObject p) throws IOException {
    return index(usersService.getPerByJson(p));
  }

  public Object index(HttpServletRequest request) throws IOException {
    return index(AesFile.request2json(request));
  }

}
