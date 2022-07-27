package com.mysl.api.service;

import com.mysl.api.entity.Group;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 权限组 服务类
 * </p>
 *
 * @author mac-xiang
 * @since 2021-05-16
 */
public interface GroupService extends IService<Group> {
  List<Group> getByBit(long group);

  Page<Group> getByBit(long group, Page<Group> page);
}
