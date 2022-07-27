package com.mysl.api.service.impl;

import com.mysl.api.lib.AesFile;
import com.mysl.api.lib.HMU;
import com.mysl.api.lib.Location;
import com.mysl.api.lib.Permission;
import com.mysl.api.entity.MediaRead;
import com.mysl.api.mapper.MediaReadMapper;
import com.mysl.api.service.MediaReadService;
import com.mysl.api.service.UsersService;

import java.util.List;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 媒体日志 服务实现类
 * </p>
 *
 * @author mac-xiang
 * @since 2021-05-16
 */
@Service
public class MediaReadServiceImpl extends ServiceImpl<MediaReadMapper, MediaRead> implements MediaReadService {
  @Autowired
  UsersService usersService;

  @Resource
  MediaReadMapper mediaReadMapper;

  public QueryWrapper<MediaRead> readWrapper(JSONObject p) {
    Permission per = usersService.getPerByJson(p);
    long start = p.getLongValue("start");
    long end = p.getLongValue("end");
    Long media = p.getLong("media");
    Long user = p.getLong("user");
    QueryWrapper<MediaRead> qmr = new QueryWrapper<>();
    qmr.orderByDesc("`media_read`.`create_time`");
    do {
      if (per.node(1, 1 << (0 + 2))) {
        long place = AesFile.Placeholder(per.group);
        if (user != null && user != per.id) {
          Permission up = usersService.getPerById(user);
          if ((up.id & place) != place) {
            break;
          }
        }
      } else {
        if (user == null) {
          user = per.id;
        } else if (user != per.id) {
          break;
        }
      }
      if (media != null) {
        qmr.eq("`media_read`.`media`", media);
      }
      if (user != null) {
        qmr.eq("`media_read`.`user`", user);
      }
      if (start > 0) {
        qmr.ge("`media_read`.`create_time`", start);
      }
      if (end > 0) {
        qmr.le("`media_read`.`create_time`", end);
      }
      return qmr;
    } while (false);
    throw new RuntimeException("没有权限");
  }

  public List<HMU> getByJson(JSONObject p) {
    return mediaReadMapper.getHMU(readWrapper(p));
  }

  public Page<HMU> getByJson(Page<MediaRead> page, JSONObject p) {
    return mediaReadMapper.getHMU(page, readWrapper(p));
  }

  public Object getObjByJson(JSONObject p) {
    Page<MediaRead> page = AesFile.getPage(p, MediaRead.class);
    return page == null ? getByJson(p) : getByJson(page, p);
  }

  public List<Location> upLocation(MediaRead mediaRead, boolean frist, Long endTime) {
    List<Location> ret = null;
    if (mediaRead.getEndTime() == 0 && endTime != null) {
      mediaRead.setEndTime(endTime);
    }
    if (mediaRead != null) {
      ret = JSONObject.parseArray(mediaRead.getLocation(), Location.class);
      int i = 0, l = ret.size();
      Location t;
      while (i < l) {
        t = ret.get(i);
        if (t.getAddress() == null) {
          t.setAddress(AesFile.getAddress(t));
          if (t.getTime() == null) {
            t.setTime(AesFile.time());
          }
          ret.set(i, t);
          continue;
        }
        if (frist) {
          break;
        }
        i++;
      }
      mediaRead.setLocation(JSONObject.toJSONString(ret));
      mediaReadMapper.updateById(mediaRead);
    }
    return ret;
  }

  public List<Location> upLocation(Long id, boolean frist, Long endTime) {
    return upLocation(mediaReadMapper.selectById(id), frist, endTime);
  }

  public void upLocationAll() {
    QueryWrapper<MediaRead> qmr = new QueryWrapper<>();
    qmr.apply("`location` not like '[{\"address\":\"%'");
    List<MediaRead> lmr = mediaReadMapper.selectList(qmr);
    Long endTime = AesFile.time();
    lmr.forEach(mr -> {
      upLocation(mr.getId(), true, endTime);
    });
  }
}
