package com.mysl.api.mapper;

import com.mysl.api.lib.HMU;
import com.mysl.api.entity.MediaRead;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 媒体日志 Mapper 接口
 * </p>
 *
 * @author mac-xiang
 * @since 2021-05-16
 */
public interface MediaReadMapper extends BaseMapper<MediaRead> {
  @Select("SELECT `have_media`.`id`,`media_read`.`id` as `mrid`,`media_read`.`create_time`,`media_read`.`end_time`,`media_read`.`user`,`media_read`.`media`,`media_read`.`location`,`media`.`type`,`media`.`title`,`media`.`img`,`media`.`simple`,`have_media`.`group`,`users`.`info_private` FROM `media_read` JOIN `media` ON `media`.`id`=`media_read`.`media` JOIN `users` ON `users`.`id`=`media_read`.`user` JOIN `have_media` ON `have_media`.`user`=`media_read`.`user` AND `have_media`.`media`=`media_read`.`media` ${ew.customSqlSegment}")
  Page<HMU> getHMU(Page<MediaRead> page, @Param(Constants.WRAPPER) Wrapper<MediaRead> haveMediaWrapper);

  @Select("SELECT `have_media`.`id`,`media_read`.`id` as `mrid`,`media_read`.`create_time`,`media_read`.`end_time`,`media_read`.`user`,`media_read`.`media`,`media_read`.`location`,`media`.`type`,`media`.`title`,`media`.`img`,`media`.`simple`,`have_media`.`group`,`users`.`info_private` FROM `media_read` JOIN `media` ON `media`.`id`=`media_read`.`media` JOIN `users` ON `users`.`id`=`media_read`.`user` JOIN `have_media` ON `have_media`.`user`=`media_read`.`user` AND `have_media`.`media`=`media_read`.`media` ${ew.customSqlSegment}")
  List<HMU> getHMU(@Param(Constants.WRAPPER) Wrapper<MediaRead> haveMediaWrapper);
}
