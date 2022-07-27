package com.mysl.api.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysl.api.lib.AesFile;
import com.mysl.api.lib.HMU;
import com.mysl.api.lib.Permission;
import com.mysl.api.entity.HaveMedia;
import com.mysl.api.mapper.HaveMediaMapper;
import com.mysl.api.service.HaveMediaService;
import com.mysl.api.service.UsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户媒体表 服务实现类
 * </p>
 *
 * @author mac-xiang
 * @since 2021-05-16
 */
@Service
public class HaveMediaServiceImpl extends ServiceImpl<HaveMediaMapper, HaveMedia> implements HaveMediaService {
  @Resource
  HaveMediaMapper haveMediaMapper;

  @Autowired
  UsersService usersService;

  public List<HaveMedia> getByWrapper(QueryWrapper<HaveMedia> wapper) {
    return haveMediaMapper.selectList(wapper);
  }

  public Page<HaveMedia> getByWrapper(Page<HaveMedia> page, QueryWrapper<HaveMedia> wapper) {
    return haveMediaMapper.selectPage(page, wapper);
  }

  private QueryWrapper<HaveMedia> getReadWrapper(JSONObject p) {
    Permission per = usersService.getPerByJson(p);
    if (per.node(0, 1 << (4 + 2))) {
      long place = AesFile.Placeholder(per.group);
      long page = p.getLongValue("page");
      long size = p.getLongValue("size");
      Long id = p.getLong("id");
      Long mid = p.getLong("media");
      Long uid = p.getLong("user");
      if (uid == null) {
        uid = per.id;
      }
      Long gid = p.getLong("group");
      if (size < 10) {
        size = 10;
      } else if (size > 100) {
        size = 100;
      }
      if (page < 0) {
        page = 0;
      }
      QueryWrapper<HaveMedia> q = new QueryWrapper<>();
      if (id != null) {
        q.eq("`have_media`.`id`", id);
      }
      if (mid != null) {
        q.eq("`have_media`.`media`", mid);
      }
      if (uid != null) {
        q.eq("`have_media`.`user`", uid);
      }
      if (gid != null) {
        q.eq("`have_media`.`group`", gid);
      }
      q.apply("(`have_media`.`group`&" + place + ")=" + per.group);
      // AesFile.clear();
      // return Result.ret(getGD.index(per));
      return q;
      // return haveMediaMapper.getHMU(q);
      // haveMediaMapper.getHMU(new Page<HaveMedia>(page, size), q)
    }
    throw new RuntimeException("没有权限");
  }

  @Override
  public List<HMU> getByJson(JSONObject p) {
    return haveMediaMapper.getHMU(getReadWrapper(p));
  }

  @Override
  public Page<HMU> getByJson(Page<HaveMedia> page, JSONObject p) {
    return haveMediaMapper.getHMU(page, getReadWrapper(p));
  }

  @Override
  public Object getObjByJson(JSONObject p) {
    Page<HaveMedia> page = AesFile.getPage(p, HaveMedia.class);
    QueryWrapper<HaveMedia> q = getReadWrapper(p);
    return page == null ? haveMediaMapper.getHMU(q) : haveMediaMapper.getHMU(page, q);
  }

}
