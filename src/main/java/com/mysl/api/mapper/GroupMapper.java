package com.mysl.api.mapper;

import com.mysl.api.entity.Group;

import org.apache.ibatis.annotations.Select;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 权限组 Mapper 接口
 * </p>
 *
 * @author mac-xiang
 * @since 2021-05-16
 */
public interface GroupMapper extends BaseMapper<Group> {
  @Select("SELECT * FROM `group` WHERE (`id` & #{complement})=(#{id} & #{complement}) AND `id`!=#{id} ORDER BY `id` ASC")
  Page<Group> selectPageByBit(Long id, long complement, Page<Group> page);

  @Select("SELECT * FROM `group` WHERE (`id` & #{complement})=(#{id} & #{complement}) AND `id`!=#{id} ORDER BY `id` ASC")
  List<Group> selectListByBit(Long id, long complement);
}
