package com.mysl.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mysl.api.entity.Media;
import com.mysl.api.entity.dto.*;
import com.mysl.api.entity.enums.CourseType;
import com.mysl.api.entity.enums.MediaType;

import java.util.List;


/**
 * @author Ivan Su
 * @date 2022/8/13
 */
public interface MediaService extends IService<Media> {

    PageInfo<MediaDTO> getMediaList(Integer pageNum, Integer pageSize, Long id, MediaType type, CourseType courseType);

    PageInfo<MediaFullDTO> getMediaList(Integer pageNum, Integer pageSize, String keyWord);

    MediaFullDTO getMediaById(Long id);

    boolean savePlayerEvent(Long id, PlayerEventDTO dto, Long storeId);

    boolean remove(Long id);

    List<MediaCategoryDTO> getCategories();

    boolean save(MediaEditDTO dto);

    boolean update(Long id, MediaEditDTO dto);
}
