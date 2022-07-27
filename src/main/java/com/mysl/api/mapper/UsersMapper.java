package com.mysl.api.mapper;

import com.mysl.api.lib.Permission;
import com.mysl.api.entity.Users;
import com.mysl.api.entity.Users2;
import com.mysl.api.entity.Users3;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;

/**
 * <p>
 * 用户 Mapper 接口
 * </p>
 *
 * @author mac-xiang
 * @since 2021-05-16
 */
public interface UsersMapper extends BaseMapper<Users> {
  // @Select("SELECT * FROM `users` ${ew.customSqlSegment}")
  // List<Users> selectList(@Param(Constants.WRAPPER) Wrapper<Users> wrapper);

  @Select("SELECT `id`,`change_time`,`permission`,`key`,`group`,`cmr` FROM `users` where id = #{id}")
  Permission getPerById(Long id);

  @Select("SELECT `key` FROM `users` where id = #{id}")
  Users getKey(Long id);

  @Update("UPDATE `users` SET `key`=#{key},`change_time`=#{changeTime} where id = #{id}")
  void setKey(Long id, byte[] key, long changeTime);

  @Update("UPDATE `users` SET `cmr`=concat(`cmr`,',',#{media}) WHERE `id`=#{id}")
  int pushCmr(long id, long media);

  @Select("SELECT * FROM `users` where id = #{id}")
  Users getById(Long id);

  @Select("SELECT * FROM `users` where `wxid` = #{wxid}")
  Users getByWxid(String wxid);

  @Select("SELECT * FROM `users` ${ew.customSqlSegment}")
  List<Users2> getList2(@Param(Constants.WRAPPER) Wrapper<Users> wrapper);

  @Select("SELECT * FROM `users` ${ew.customSqlSegment}")
  List<Users3> getList3(@Param(Constants.WRAPPER) Wrapper<Users> wrapper);

  @Select("SELECT * FROM `users` WHERE `username`=#{username} LIMIT 0,1")
  Users getByUsername(String username);

}
