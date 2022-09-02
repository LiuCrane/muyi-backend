package com.mysl.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mysl.api.entity.Media;
import com.mysl.api.entity.dto.MediaFullDTO;
import com.mysl.api.entity.enums.MediaType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/13
 */
public interface MediaMapper extends BaseMapper<Media> {

    List<MediaFullDTO> findAll(@Param("id") Long id,
                               @Param("type") MediaType type,
                               @Param("publicly") Boolean publicly,
                               @Param("title") String title,
                               @Param("description") String description,
                               @Param("category") String category,
                               @Param("key_word") String keyWord);

    int sumMediaDuration(@Param("media_ids") List<Long> mediaIds);

    int countUnfinishedMedia(@Param("class_id") Long classId, @Param("course_id") Long courseId);
}
