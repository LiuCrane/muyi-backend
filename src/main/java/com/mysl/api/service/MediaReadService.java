package com.mysl.api.service;

import com.mysl.api.lib.HMU;
import com.mysl.api.lib.Location;
import com.mysl.api.entity.MediaRead;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 媒体日志 服务类
 * </p>
 *
 * @author mac-xiang
 * @since 2021-05-16
 */
public interface MediaReadService extends IService<MediaRead> {
  QueryWrapper<MediaRead> readWrapper(JSONObject p);

  List<HMU> getByJson(JSONObject p);

  Page<HMU> getByJson(Page<MediaRead> page, JSONObject p);

  Object getObjByJson(JSONObject p);

  List<Location> upLocation(MediaRead mediaRead, boolean frist, Long endTime);

  List<Location> upLocation(Long id, boolean frist, Long endTime);

  void upLocationAll();
}
