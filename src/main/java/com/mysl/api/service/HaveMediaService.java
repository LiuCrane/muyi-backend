package com.mysl.api.service;

import com.mysl.api.lib.HMU;
import com.mysl.api.entity.HaveMedia;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户媒体表 服务类
 * </p>
 *
 * @author mac-xiang
 * @since 2021-05-16
 */
public interface HaveMediaService extends IService<HaveMedia> {
  List<HaveMedia> getByWrapper(QueryWrapper<HaveMedia> wapper);

  Page<HaveMedia> getByWrapper(Page<HaveMedia> page, QueryWrapper<HaveMedia> wapper);

  List<HMU> getByJson(JSONObject p);

  Page<HMU> getByJson(Page<HaveMedia> page, JSONObject p);

  Object getObjByJson(JSONObject p);
}
