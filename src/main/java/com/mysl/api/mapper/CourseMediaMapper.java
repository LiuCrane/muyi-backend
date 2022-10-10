package com.mysl.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mysl.api.entity.CourseMedia;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/14
 */
public interface CourseMediaMapper extends BaseMapper<CourseMedia> {

    List<Long> findMediaIds(@Param("course_id") Long courseId);

    int deleteByCourseId(@Param("course_id") Long courseId);

    int deleteOne(@Param("course_id") Long courseId, @Param("media_id") Long mediaId);
}
