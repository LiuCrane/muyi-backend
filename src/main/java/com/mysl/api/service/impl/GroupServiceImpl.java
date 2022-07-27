package com.mysl.api.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysl.api.lib.AesFile;
import com.mysl.api.entity.Group;
import com.mysl.api.mapper.GroupMapper;
import com.mysl.api.service.GroupService;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 权限组 服务实现类
 * </p>
 *
 * @author mac-xiang
 * @since 2021-05-16
 */
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {
  @Resource
  public GroupMapper groupMapper;

  public List<Group> getByBit(long group) {

    return groupMapper.selectListByBit(group, AesFile.Placeholder(group));
  }

  public Page<Group> getByBit(long group, Page<Group> page) {
    return groupMapper.selectPageByBit(group, AesFile.Placeholder(group), page);
  }
}
