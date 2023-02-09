package com.mysl.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mysl.api.entity.MediaPlayEvent;
import com.mysl.api.entity.enums.PlayerEvent;
import org.apache.ibatis.annotations.Param;

/**
 * @author Ivan Su
 * @date 2022/8/16
 */
public interface MediaPlayEventMapper extends BaseMapper<MediaPlayEvent> {

    MediaPlayEvent getEvent(@Param("class_course_id") Long classCourseId,
                            @Param("media_id") Long mediaId,
                            @Param("event") PlayerEvent event,
                            @Param("user_id") Long userId,
                            @Param("desc") Boolean desc);

    int updateRecorded(@Param("class_course_id")Long classCourseId,
                       @Param("media_id") Long mediaId,
                       @Param("user_id") Long userId);

    int countEndedMedia(@Param("store_id") Long storeId,
                        @Param("media_id") Long mediaId,
                        @Param("class_course_id") Long classCourseId);
}
