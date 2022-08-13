package com.mysl.api.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.mysl.api.common.lang.Result;
import com.mysl.api.lib.AesFile;
import com.mysl.api.lib.Permission;
import com.mysl.api.lib.UrlId;
import com.mysl.api.entity.MediaV1;
import com.mysl.api.mapper.HaveMediaMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Upload {

  public String saveImg(Part file, String path) throws IOException {
    String ret = file.getSubmittedFileName();
    ret = ret.substring(ret.lastIndexOf("."));
    ret = IdWorker.getId() + ret;
    String t = AesFile.formatFilename(path + "/" + ret);
    File dest = new File(t);
    if (!dest.getParentFile().exists()) {
      dest.getParentFile().mkdirs();
    }

    file.write(t);
    return ret;
  }

  private void run(String cmd) throws Exception {
    Process p = Runtime.getRuntime().exec(cmd);
    InputStream stderr = p.getErrorStream();
    InputStreamReader isr = new InputStreamReader(stderr);
    BufferedReader br = new BufferedReader(isr);
    String line = null;

    StringBuffer sb = new StringBuffer();
    while ((line = br.readLine()) != null) {
      sb.append(line);
    }
  }

  private UrlId saveMedia(Part file, boolean pub) throws Exception {
    long id = IdWorker.getId();
    String ret = (pub ? "public/upload/media/" : "crypto/") + id, s = file.getContentType(), p;
    String[] pa = { "fileItem", "tempFile", "path" };
    Object path = AesFile.getPrivate(file, pa);
    if (path == null) {
      throw null;
    }
    if (s.equals("video/mp4")) {
      ret += "/index.m3u8";
      p = AesFile.formatFilename(ret, true);
      run("ffmpeg -y -i " + (String) path + " -hls_time 5 -hls_list_size 0 -f hls " + p);
      s = new String(AesFile.readFile(p));
      int i = s.indexOf("#EXTINF");
      s = s.substring(0, i) + "#EXT-X-KEY:METHOD=AES-128,URI=\"/encrypt.key\",IV=0x00000000000000000000000000000000\n"
          + s.substring(i);
      AesFile.writeFile(p, s.getBytes());
    } else if (s.substring(0, 5).equals("audio")) {
      ret += ".aac";
      p = AesFile.formatFilename(ret);
      run("ffmpeg -y -i " + (String) path + " -c:a aac " + p);
    } else {
      throw null;
    }

    return new UrlId(id, ret.substring(6));
  }

  @Autowired
  UsersService usersService;

  @Autowired
  MediaV1Service mediaV1Service;

  @Resource
  private HaveMediaMapper mapper;

  public Object index(HttpServletRequest request) {
    try {
      Part file = request.getPart("pic");
      String name = request.getParameter("public");
      boolean pub = name != null;
      if (pub && (name.equals("0") || name.equals("false"))) {
        pub = false;
      }
      name = request.getParameter("name");
      Permission per = usersService.useToken(request.getParameter("token"));
      // byte[] b = { 1, 2, 3 };
      // per = new Permission(b);
      // 新增 rucd 0-3 全局 4-7 组
      if (per == null || !(per.node(1, 1 << 2) || per.node(1, 1 << 4)) || file == null || name == null
          || !file.getContentType().substring(0, 5).equals("image")) {
        throw new Exception("没有权限或文件格式错误");
      }

      String pic = "upload/img/";
      pic += saveImg(file, "public/" + pic);

      UrlId ui = saveMedia(request.getPart("media"), pub);
      String media = ui.getUrl();
      MediaV1 d = new MediaV1();
      d.setId(ui.id);
      d.setCreateTime(AesFile.time());
      d.setType(media.substring(media.lastIndexOf(".") + 1));
      d.setState(1);
      d.setTitle(name);
      d.setImg(pic);
      String simple = request.getParameter("simple");
      d.setSimple(simple == null ? "" : simple);
      d.setUrl("");
      mediaV1Service.saveOrUpdate(d);

      // MediaChange mc = new MediaChange();
      // mc.setCreateTime(d.getCreateTime());
      // mc.setUser(per.id);
      // mc.setMedia(ui.id);
      // mc.setContent("添加媒体");
      // mediaChangeService.save(mc);

      return Result.ret(d);
    } catch (Exception e) {
      // e.printStackTrace();
      return Result.ret(404, e.getMessage());
      // return mapper.getByUser(2l);
    }
  }
}
