package com.mysl.api.service;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.mysl.api.lib.AesFile;
import com.mysl.api.lib.Permission;
import com.mysl.api.entity.HaveMedia;
import com.mysl.api.entity.MediaRead;
import com.mysl.api.entity.Users;
import com.mysl.api.mapper.HaveMediaMapper;
import com.mysl.api.mapper.UsersMapper;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ReadFile {

  @Autowired
  UsersService usersService;
  @Autowired
  MediaReadService mediaReadService;
  @Autowired
  HaveMediaService haveMediaService;

  @Resource
  HaveMediaMapper haveMediaMapper;

  @Resource
  UsersMapper usersMapper;

  private void createMediaRead(long uid, long mid, Permission per) {
    long time = AesFile.time() - 600;
    List<MediaRead> t = mediaReadService
        .list(new QueryWrapper<MediaRead>().eq("`user`", uid).eq("media", mid).gt("`create_time`", time));

    // log.info(t.size());
    // log.info(time);
    // t.forEach(System.out::println);

    if (t.size() > 0) {
      return;
    }

    QueryWrapper<HaveMedia> hmq = new QueryWrapper<>();
    hmq.eq("media", mid).eq("user", per.id).and(i -> i.gt("`read`", 0).or().eq("`read`", -1));
    HaveMedia hm = haveMediaMapper.selectOne(hmq);
    if (hm == null) {
      if (per.permission[1] == 255 || per.permission[1] == -1) {
        hm = new HaveMedia();
        hm.setId(IdWorker.getId());
        hm.setMedia(mid);
        hm.setRead(-1);
        hm.setUser(per.id);
        hm.setGroup(0l);
        haveMediaService.save(hm);
      } else {
        throw new RuntimeException("没有权限 或 阅读次数用尽");
      }
    }
    if (!per.node(3, 1)) {
      if (hm.getRead() > 0) {
        HaveMedia whm = new HaveMedia();
        whm.setId(hm.getId());
        whm.setRead(hm.getRead() - 1);
        haveMediaMapper.updateById(whm);
      } else {
        throw new RuntimeException("次数已用尽");
      }
    }

    MediaRead mr = new MediaRead();
    mr.setId(IdWorker.getId());
    mr.setCreateTime(AesFile.time());
    mr.setUser(uid);
    mr.setMedia(mid);
    mr.setLocation("[]");
    mediaReadService.save(mr);
    String cmr = (per.cmr != null && per.cmr.length() > 2) ? per.cmr : "[]";
    List<Long> o = JSONObject.parseArray(cmr, Long.class);
    o.add(mr.getId());
    while (o.toString().length() > 65535) {
      o.remove(1);
    }
    cmr = JSONObject.toJSONString(o).replaceAll("\\s", "");
    Users u = new Users();
    u.setId(uid);
    u.setCmr(cmr);
    usersService.updateById(u);
  }

  public void crypto(HttpServletRequest request, HttpServletResponse response) throws Exception {
    // 音频 /crypto/用户id/媒体id.aac
    // 视频 /crypto/用户id/媒体id/index.m3u8
    String url = request.getRequestURI();

    String str = url.substring(8);
    int i = str.indexOf("/"), i2;
    String ids = str.substring(0, i);
    long uid = Long.parseLong(ids);
    i2 = str.indexOf("/", i + 1);
    if (i2 < 0) {
      i2 = str.indexOf(".", i + 1);
    }
    ids = str.substring(i + 1, i2);
    long mid = Long.parseLong(ids);

    str = AesFile.formatFilename("crypto" + str.substring(i), true);

    ids = AesFile.formatFilename("crypto", true);
    if (str.indexOf(ids) != 0) {
      throw new RuntimeException("路径非法");
    }
    Permission per = usersService.getPerById(uid);
    // if (AesFile.time() - per.changeTime > 600) {
    // response.sendError(404);
    // return;
    // }
    if (!per.node(0, 6)) { // 读取全部媒体权限
      // HaveMedia hm = haveMediaMapper.getUserMedia(uid, mid);
      HaveMedia hm = haveMediaMapper
          .selectOne(new QueryWrapper<HaveMedia>().eq("`user`", uid).eq("`media`", mid).last("LIMIT 1"));
      if (hm == null) {
        response.sendError(404);
        return;
      }
    }

    // byte[] k = usersService.getKey(uid);
    byte[] d = AesFile.readFile(str);
    String mime = AesFile.mime(str.substring(str.lastIndexOf(".") + 1));
    response.setHeader("content-type", mime);
    if (mime.equals("application/vnd.apple.mpegurl")) {
      createMediaRead(uid, mid, per);
    } else {
      if (mime.equals("audio/aac")) {
        createMediaRead(uid, mid, per);
      }
      d = AesFile.aes(d, per.key);
    }
    response.getOutputStream().write(d);
  }

  public void index(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String str = request.getRequestURI();

    str = AesFile.formatFilename("public" + str);
    log.info("after format: {}", str);
    String mime = AesFile.formatFilename("public", true);
    log.info("mime: {}", mime);
    if (str.indexOf(mime) != 0) {
      throw new RuntimeException("路径非法");
    }

    File fi = new File(str);
    while (!fi.exists() || fi.isDirectory()) {
      str += "/index.html";
      try {
        fi = new File(str);
        if (!fi.exists()) {
          response.sendError(404, "别费劲了,没这玩意");
          // response.sendError(404);
          // response.sendRedirect("/error.html");
          return;
        }
      } catch (Exception e) {
        response.reset();
        return;
      }
    }

    byte[] d = AesFile.readFile(str);
    mime = AesFile.mime(str.substring(str.lastIndexOf(".") + 1));
    response.setHeader("content-type", mime);
    response.getOutputStream().write(d);

  }

}
