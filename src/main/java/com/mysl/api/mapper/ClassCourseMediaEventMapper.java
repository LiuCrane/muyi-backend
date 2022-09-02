package com.mysl.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mysl.api.entity.ClassCourseMediaEvent;
import com.mysl.api.entity.enums.PlayerEvent;
import org.apache.ibatis.annotations.Param;

/**
 * @author Ivan Su
 * @date 2022/8/16
 */
public interface ClassCourseMediaEventMapper extends BaseMapper<ClassCourseMediaEvent> {

    ClassCourseMediaEvent getEvent(@Param("class_course_id") Long classCourseId,
                                   @Param("media_id") Long mediaId,
                                   @Param("event") PlayerEvent event,
                                   @Param("desc") Boolean desc);

    int updateRecorded(@Param("class_course_id")Long classCourseId, @Param("media_id") Long mediaId);
}
