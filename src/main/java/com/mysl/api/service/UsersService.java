package com.mysl.api.service;

import com.mysl.api.lib.Permission;
import com.mysl.api.entity.Users;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户 服务类
 * </p>
 *
 * @author mac-xiang
 * @since 2021-05-16
 */
public interface UsersService extends IService<Users> {
  Object getToken(String username, String password, int timeOut);

  String getToken(Users user);

  String getToken(long id);

  String getTokenByWxid(String wxid);

  Permission useToken(String token);

  Permission getPerByToken(String token);

  Permission getPerByJson(JSONObject p);

  Permission getPerById(long id);

  byte[] getKey(long id);

  byte[] setKey(long id);

  // Users getById(long id);

  Users getByToken(String token);

  List<Long> updateCmr(Permission per);

}
