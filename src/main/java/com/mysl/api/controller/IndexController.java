package com.mysl.api.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mysl.api.common.lang.Result;
import com.mysl.api.lib.AesFile;
import com.mysl.api.lib.Location;
import com.mysl.api.lib.Permission;
import com.mysl.api.service.GetGD;
import com.mysl.api.service.GetOpenId;
import com.mysl.api.service.ReadFile;
import com.mysl.api.service.Upload;
import com.mysl.api.entity.HaveMedia;
import com.mysl.api.entity.MediaRead;
import com.mysl.api.entity.Users;
import com.mysl.api.entity.Users2;
import com.mysl.api.mapper.HaveMediaMapper;
import com.mysl.api.mapper.MediaMapper;
import com.mysl.api.mapper.Users2Mapper;
import com.mysl.api.mapper.Users3Mapper;
import com.mysl.api.mapper.UsersMapper;
import com.mysl.api.service.HaveMediaService;
import com.mysl.api.service.MediaReadService;
import com.mysl.api.service.UsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
  @Resource
  private HaveMediaMapper haveMediaMapper;

  @Resource
  private MediaMapper mediaMapper;

  @Resource
  private UsersMapper usersMapper;
  @Resource
  private Users2Mapper users2Mapper;
  @Resource
  private Users3Mapper users3Mapper;

  @Autowired
  UsersService usersService;

  @Autowired
  Upload upload;

  @Autowired
  MediaReadService mediaReadService;

  @Autowired
  HaveMediaService haveMediaService;

  @Autowired
  GetGD getGD;

  @Autowired
  ReadFile readFile;

  // @GetMapping("/")
  // public Object index2(HttpServletRequest request) throws Exception {
  //   return "123456";
  // }

  @GetMapping("/crypto/**")
  public void crypto(HttpServletRequest request, HttpServletResponse response) throws Exception {
    readFile.crypto(request, response);
  }

  @GetMapping("/**")
  public void index(HttpServletRequest request, HttpServletResponse response) throws Exception {
    readFile.index(request, response);
  }

  @PostMapping({ "gd" })
  public Object gd(HttpServletRequest request) throws IOException {
    return getGD.index(request);
  }

  // #region login
  @CrossOrigin(origins = "*")
  @PostMapping("login")
  public Object login(HttpServletRequest request) throws IOException {
    String username = request.getParameter("username");
    if (username == null) {
      return login1(AesFile.request2json(request));
    }

    String password = request.getParameter("password");
    int timeOut = 0;
    try {
      timeOut = Integer.parseInt(request.getParameter("timeout"));
    } catch (Exception e) {
    }
    return usersService.getToken(username, password, timeOut);
  }

  @Autowired
  GetOpenId getOpenId;

  public Object login1(JSONObject param) {
    Object t = param.get("username");
    if (t != null && t.getClass().getName().equals("java.lang.String")) {
      String username = t.toString();
      int l = username.length();
      if (l > 4 && l < 16) {
        t = param.get("password");
        if (t.getClass().getName().equals("java.lang.String")) {
          String password = t.toString();
          l = password.length();
          if (l > 4 && l < 16) {
            t = param.get("timeOut");
            try {
              l = Integer.parseInt(t.toString());
            } catch (Exception e) {
              l = 0;
            }
            return usersService.getToken(username, password, l);
          }
        }
      }
    } else {
      String ret = param.getString("code");
      if (ret != null) {
        switch (param.getString("method")) {
        case "wxid":
          ret = usersService.getTokenByWxid(getOpenId.wx(ret));
          break;

        default:
          ret = usersService.getTokenByWxid(getOpenId.wx(ret));
          break;
        }
        return Result.ret((Object) ret);
      }
    }
    return Result.ret(1, "数据格式错误");
  }

  @PostMapping("changeAppid")
  public Object changeAppid(HttpServletRequest request) {
    JSONObject p = AesFile.request2json(request);
    String code = p.getString("code");
    if (code == null) {
      return Result.ret(101, "格式错误");
    }
    Permission per = usersService.getPerByJson(p);
    Users users = new Users();
    users.setId(per.id);
    switch (p.getString("method")) {
    case "wxid":
      users.setWxid(getOpenId.wx(code));
      break;

    default:
      return Result.ret(101, "没有权限");
    }
    return Result.ret(usersService.updateById(users));
  }
  // #endregion

  @PostMapping("use")
  public Object useToken(@RequestBody Map<String, Object> param) {
    Object ret = null;
    try {
      Users u = usersService.getByToken(param.get("token").toString());
      if (u == null) {
        ret = Result.ret(2, "token非法");
      } else {
        // Users2 r = u.clone;
        u.setKey(usersService.setKey(u.getId()));
        ret = Result.ret(new Users2(u));
      }
    } catch (Exception e) {
      ret = Result.ret(1, "格式错误");
    }
    return ret;
  }

  @PostMapping("upload")
  public Object upload(HttpServletRequest request) throws Exception {
    String path = request.getParameter("dir");
    do {
      if (path == null) {
        break;
      }
      String u = "upload/", t = request.getParameter("path");
      if (t != null) {
        if (t.length() > 0 && !t.substring(0, 1).equals("/")) {
          t = "/" + t;
        }
        path += t;
      }
      path = u + path;
      t = AesFile.formatFilename("public/");
      u = AesFile.formatFilename(t + "/" + path);
      if (u.indexOf(t) != 0) {
        break;
      }
      t = upload.saveImg(request.getPart("imgFile"), u);
      if (t != null) {
        return Result.ret(0, "成功", (Object) (path + "/" + t));
      }
    } while (false);
    return Result.ret(404, "参数错误");
  }


  @PostMapping("mediaList")
  public Object mediaList(HttpServletRequest request) {
    // ModelAndView mav = new ModelAndView("/sql/haveMedia/read"); // 绝对路径OK
    JSONObject p = AesFile.request2json(request);
    return Result.ret(haveMediaService.getObjByJson(p));
  }

  @PostMapping("reg")
  public Object reg(HttpServletRequest request) {
    JSONObject param;
    try {
      param = AesFile.request2json(request);
    } catch (Exception e) {
      return Result.ret(402, "参数错误");
    }
    JSONObject pri = param.getJSONObject("infoPrivate"), pub = param.getJSONObject("infoPublic");
    String username = param.getString("username"), password = param.getString("password");
    if (pri.isEmpty() || pub.isEmpty() || username == null || username.length() > 16 || username.length() < 5
        || password == null || password.length() > 16 || password.length() < 5) {
      return Result.ret(401, "数据格式错误");
    }
    Users u = new Users();
    // u.setId(IdWorker.getId());
    u.setCreateTime(AesFile.time());
    u.setGroup(0l);
    u.setState(1);
    u.setUsername(username);
    u.setPassword(password);
    u.setInfoPrivate(pri.toJSONString());
    u.setInfoPublic(pub.toJSONString());
    u.setCmr("");
    u.setWxid(IdWorker.get32UUID());
    boolean b;
    try {
      b = usersService.save(u);
    } catch (Exception e) {
      b = false;
    }
    return b ? usersService.getToken(username, password, 0) : Result.ret(403, "用户名重复或其他错误.");
  }

  @PostMapping("mediaLib")
  public Object mediaLib(HttpServletRequest request) {
    JSONObject param;
    Permission per = null;
    try {
      param = AesFile.request2json(request);
      per = usersService.useToken(param.getString("token"));
    } catch (Exception e) {
      return Result.ret(402, "参数错误");
    }
    long id = param.getLongValue("id"), user = param.getLongValue("user"), group = param.getLongValue("group"),
        media = param.getLongValue("media");
    int page = param.getIntValue("page"), rucd = param.getIntValue("rucd"); // 0: 读取; 1: 修改; 2:增加; 3: 删除; 4: 物理删除;

    if (per == null) {
      return Result.ret(401, "数据格式错误");
    }
    Users ud = usersService.getById(user);
    QueryWrapper<HaveMedia> hmq = new QueryWrapper<>();
    Object ret = null;
    switch (rucd) { // node 2
    case 0: // 读取 媒体列表
      // new Page<Users2>(page, 10)
      if (per.node(2, 1)) { // 全局
      } else if (per.node(2, 1 << 4)) { // 组
        hmq.eq("group", per.group);
      } else {
        hmq.eq("user", per.id);
      }
      if (media > 0l) {
        hmq.eq("media", media);
      }
      if (user > 0l) {
        hmq.eq("user", user);
      }
      ret = Result.ret(haveMediaMapper.selectPage(new Page<HaveMedia>(page, 10), hmq));
      break;
    case 1: // 修改 数据
      if (!(id > 0l)) {
        ret = Result.ret(101, "缺少id");
      } else {
        hmq.eq("id", id);
        HaveMedia hm = new HaveMedia();
        hm.setId(id);
        if (per.node(2, 1 << rucd)) {
          hm.setMedia(media);
          hm.setGroup(group);
          hm.setUser(user);
        } else if (per.node(2, 1 << (4 + rucd))) {
          hmq.eq("group", group);
          hm.setMedia(media);
          hm.setUser(user);
        } else {
          hmq.eq("id", 0);
        }
        ret = Result.ret(haveMediaService.update(hm, hmq));
      }
      break;
    case 2: // 增加
      HaveMedia hm = new HaveMedia();
      if (per.node(2, 1 << rucd)) {
        hm.setGroup(group);
        hm.setMedia(media);
        hm.setUser(user);
      } else if (per.node(2, 1 << (4 + rucd)) && (user == 0 || ud.getGroup() == per.group)) {
        hm.setMedia(media);
        hm.setUser(user);
      } else {
        ret = Result.ret(1, "没有权限");
        break;
      }
      ret = Result.ret(haveMediaService.save(hm));
      break;
    case 4: // 删除文件
      if (per.node(2, 1 << 3)) {
        rucd = 3;
        if (id > 0) {
          hmq.eq("id", id);
        } else {
          if (media > 0) {
            hmq.eq("media", media);
          }
          if (user > 0) {
            hmq.eq("user", user);
          }
          if (group > 0) {
            hmq.eq("group", group);
          }
        }
        List<HaveMedia> hms = haveMediaMapper.selectList(hmq);
        hmq = new QueryWrapper<>();
        String ts = null;
        for (HaveMedia thm : hms) { // 删除文件
          ts = AesFile.fileExists("crypto/" + thm.getGroup());
          if (ts == null) {
            ts = AesFile.fileExists("crypto/" + thm.getGroup() + ".aac");
          }
          if (ts != null) {
            AesFile.DeleteFolder(ts);
          }
        }
      } else {
        ret = Result.ret(1, "没有权限");
        break;
      }
    case 3:
      if (per.node(2, 1 << rucd)) {
        if (id > 0) {
          hmq.eq("id", id);
        } else {
          if (media > 0) {
            hmq.eq("media", media);
          }
          if (user > 0) {
            hmq.eq("user", user);
          }
          if (group > 0) {
            hmq.eq("group", group);
          }
        }
      } else if (per.node(2, 1 << (4 + rucd)) && id > 0) {
        hmq.eq("id", id);
        hmq.eq("group", per.group);
      } else {
        hmq.eq("id", 0);
      }
      ret = Result.ret(haveMediaMapper.delete(hmq));
      break;

    default:
      ret = Result.ret(2, "参数错误");
      break;
    }

    return ret;
  }

  @PostMapping("location")
  public Object location(HttpServletRequest request) throws IOException {
    JSONObject p = AesFile.request2json(request);
    Permission per = usersService.getPerByJson(p);
    Location location = new Location();
    location.setTime(AesFile.time());
    location.setLatitude(p.getDouble("latitude"));
    location.setLongitude(p.getDouble("longitude"));
    if (location.latitude != null && location.longitude != null) {
      Long endTime = p.getLong("media") == null ? null : AesFile.time();
      String cmr = (per.cmr != null && per.cmr.length() > 2) ? per.cmr : "[]";
      List<Long> o = JSONObject.parseArray(cmr, Long.class);
      List<MediaRead> mrs = null;
      if (o.size() > 0) {
        QueryWrapper<MediaRead> qmr = new QueryWrapper<>();
        qmr.in("`id`", o);
        mrs = mediaReadService.list(qmr);
        if (mrs != null) {
          mrs.forEach(mr -> {
            List<Location> t;
            try {
              t = JSONObject.parseArray(mr.getLocation(), Location.class);
            } catch (Exception e) {
              t = new ArrayList<>();
            }
            t.add(location);
            mr.setLocation(JSONObject.toJSONString(t));
            mediaReadService.upLocation(mr, true, endTime);
          });
        }

        if (endTime != null) {
          Users u = new Users();
          u.setId(per.id);
          u.setCmr("[]");
          usersService.updateById(u);
        }
      }
      return Result.ret(o);
    }
    return Result.ret(101, "没有权限");
  }
  // public ModelAndView location() {
  // ModelAndView mav = new ModelAndView("/sql/mediaRead/update"); // 绝对路径OK
  // return mav;
  // }

}
