package com.mysl.api.mapper;

import com.mysl.api.lib.HMU;
import com.mysl.api.entity.HaveMedia;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 用户媒体表 Mapper 接口
 * </p>
 *
 * @author mac-xiang
 * @since 2021-05-16
 */
public interface HaveMediaMapper extends BaseMapper<HaveMedia> {
  @Select("SELECT `id`,`media`,`user` FROM `have_media` where user = #{user}")
  List<HaveMedia> getByUser(Long user);

  @Select("SELECT `id`,`media`,`user` FROM `have_media` where user = #{user} and media=#{media} LIMIT 0,1")
  HaveMedia getUserMedia(Long user, long media);

  @Select("SELECT `have_media`.*, `media`.`create_time`,`media`.`type`,`media`.`title`,`media`.`img`,`media`.`simple` FROM `have_media` JOIN `media` ON `media`.`id`=`have_media`.`media` ${ew.customSqlSegment}")
  Page<HMU> getHMU(Page<HaveMedia> page, @Param(Constants.WRAPPER) Wrapper<HaveMedia> haveMediaWrapper);

  @Select("SELECT `have_media`.*, `media`.`create_time`,`media`.`type`,`media`.`title`,`media`.`img`,`media`.`simple` FROM `have_media` JOIN `media` ON `media`.`id`=`have_media`.`media` ${ew.customSqlSegment}")
  List<HMU> getHMU(@Param(Constants.WRAPPER) Wrapper<HaveMedia> haveMediaWrapper);
}
