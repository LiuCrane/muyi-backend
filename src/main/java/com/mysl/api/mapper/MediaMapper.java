package com.mysl.api.mapper;

import com.mysl.api.entity.Media;

import org.apache.ibatis.annotations.Select;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 媒体库 Mapper 接口
 * </p>
 *
 * @author mac-xiang
 * @since 2021-05-16
 */
public interface MediaMapper extends BaseMapper<Media> {
  @Select("SELECT `media`.* FROM `have_media` RIGHT JOIN `media` ON `have_media`.`media`=`media`.`id` WHERE `have_media`.`user`=#{userId} ORDER BY `media`.`sort` DESC , `media`.`change_time` DESC LIMIT #{start},10")
  List<Media> getByUser(long userId, int start);

  @Select("SELECT `media`.* FROM `have_media` RIGHT JOIN `media` ON `have_media`.`media`=`media`.`id` WHERE `have_media`.`user`=#{userId} AND `media`.`type`='#{type}' ORDER BY `media`.`sort` DESC , `media`.`change_time` DESC LIMIT #{start},10")
  List<Media> getByUserType(long userId, String type, int start);

  @Select("SELECT * FROM `media` WHERE `type`='${type}' ORDER BY `media`.`sort` DESC , `media`.`change_time` DESC LIMIT #{start},10")
  List<Media> getByType(String type, int start);

  @Select("SELECT * FROM `media` ORDER BY `media`.`sort` DESC , `media`.`change_time` DESC LIMIT #{start},10")
  List<Media> getAll(int start);

  @Select("SELECT DISTINCT `type` FROM `media`")
  List<Media> getTypes();
}
