package com.mysl.api.mapper;

import com.mysl.api.lib.Permission;
import com.mysl.api.entity.Users3;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;

public interface Users3Mapper extends BaseMapper<Users3> {

  @Select("SELECT `id`,`change_time`,`permission`,`key`,`group` FROM `users` where id = #{id}")
  Permission getPermission(Long id);

  @Select("SELECT `key` FROM `users` where id = #{id}")
  Users3 getKey(Long id);

  @Update("UPDATE `users` SET `key`=#{key},`change_time`=#{changeTime} where id = #{id}")
  void setKey(Long id, byte[] key, long changeTime);

  @Select("SELECT * FROM `users` where id = #{id}")
  Users3 getById(Long id);

  @Select("SELECT * FROM `users` ${ew.customSqlSegment}")
  List<Users3> getList3(@Param(Constants.WRAPPER) Wrapper<Users3> wrapper);

}