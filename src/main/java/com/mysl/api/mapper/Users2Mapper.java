package com.mysl.api.mapper;

import com.mysl.api.lib.Permission;
import com.mysl.api.entity.Users2;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;

public interface Users2Mapper extends BaseMapper<Users2> {
  @Select("SELECT * FROM `users` ${ew.customSqlSegment}")
  List<Users2> selectList(@Param(Constants.WRAPPER) Wrapper<Users2> wrapper);

  @Select("SELECT `id`,`change_time`,`permission`,`key`,`group` FROM `users` where id = #{id}")
  Permission getPermission(Long id);

  @Select("SELECT `key` FROM `users` where id = #{id}")
  Users2 getKey(Long id);

  @Update("UPDATE `users` SET `key`=#{key},`change_time`=#{changeTime} where id = #{id}")
  void setKey(Long id, byte[] key, long changeTime);

  @Select("SELECT * FROM `users` where id = #{id}")
  Users2 getById(Long id);

  @Select("SELECT * FROM `users` ${ew.customSqlSegment}")
  List<Users2> getList2(@Param(Constants.WRAPPER) Wrapper<Users2> wrapper);

}