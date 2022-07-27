package com.mysl.api.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.AES;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysl.api.common.lang.Result;
import com.mysl.api.lib.AesFile;
import com.mysl.api.lib.Permission;
import com.mysl.api.lib.Token;
import com.mysl.api.entity.MediaRead;
import com.mysl.api.entity.Users;
import com.mysl.api.mapper.UsersMapper;
import com.mysl.api.service.MediaReadService;
import com.mysl.api.service.UsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author mac-xiang
 * @since 2021-05-16
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {
  private static String key = "6d37a8d76138e7ad"; // 秘钥
  @Resource
  private UsersMapper usersMapper;

  @Autowired
  MediaReadService mediaReadService;

  public List<Long> updateCmr(Permission per) {
    String cmr = (per.cmr != null && per.cmr.length() > 2) ? per.cmr : "[]";
    List<Long> o = JSONObject.parseArray(cmr, Long.class);
    int len = o.size(), i = 0;
    long endTime = AesFile.time(), time = endTime - 86400;
    MediaRead tmr = null;
    while (i < len) {
      tmr = mediaReadService.getById(o.get(i));
      if (tmr == null || tmr.getEndTime() != 0 || tmr.getCreateTime() < time) {
        if (tmr != null && (tmr.getEndTime() == 0 || tmr.getLocation().indexOf("address") < 0)) {
          mediaReadService.upLocation(tmr.getId(), true, endTime);
        }
        len--;
        o.remove(i);
        continue;
      }
      i++;
    }
    Users u = new Users();
    u.setId(per.id);
    u.setCmr(JSONObject.toJSONString(o));
    usersMapper.updateById(u);
    return o;
  }

  public String getToken(Users user) {
    Token tk = new Token();
    tk.setId(user.getId());
    tk.setPassword(user.getPassword());
    updateCmr(user.toPer());
    return AES.encrypt(JSON.toJSONString(tk), key);
  }

  public Object getToken(String username, String password, int timeOut) {
    QueryWrapper<Users> wrapper = new QueryWrapper<>();
    wrapper.eq("username", username);
    wrapper.eq("password", password);
    wrapper.gt("state", 0);

    List<Users> us = usersMapper.selectList(wrapper);
    if (us.size() > 0) {
      return Result.ret((Object) getToken(us.get(0)));
    }
    return Result.ret(1, "用户名或密码错误!");
  }

  public String getToken(long id) {
    return getToken(usersMapper.getById(id));
  }

  public String getTokenByWxid(String wxid) {
    Users user = usersMapper.getByWxid(wxid);
    if (user == null) {
      throw new RuntimeException("用户不存在:" + wxid);
    }
    return getToken(user);
  }

  public Permission useToken(String token) {
    try {
      JSONObject de = JSON.parseObject(AES.decrypt(token, key));
      return getPerById(de.getLongValue("id"));
    } catch (Exception e) {
      // e.printStackTrace();
    }
    throw new RuntimeException("token非法");
  }

  public Permission getPerByToken(String token) {
    return useToken(token);
  }

  public Permission getPerByJson(JSONObject p) {
    return useToken(p.getString("token"));
  }

  public Permission getPerById(long id) {
    Permission ret = usersMapper.getPerById(id);
    if (ret == null) {
      throw new RuntimeException("用户不存在");
    }
    updateCmr(ret);
    return ret;
  }

  public byte[] getKey(long id) {
    Users u = usersMapper.getKey(id);
    return u.getKey();
  }

  public byte[] setKey(long id) {
    byte[] r = AesFile.randByte(16);
    Users u = new Users();
    u.setId(id);
    u.setChangeTime(AesFile.time());
    u.setKey(r);
    // usersMapper.setKey(id, r, AesFile.time());
    this.updateById(u);
    return r;
  }

  public Users getById(long id) {
    return usersMapper.getById(id);
  }

  public Users getByToken(String token) {
    JSONObject de = JSON.parseObject(AES.decrypt(token, key));
    long id = de.getLongValue("id");
    return usersMapper.getById(id);
  }

}
