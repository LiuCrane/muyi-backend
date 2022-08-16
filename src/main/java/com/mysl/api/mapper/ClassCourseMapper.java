package com.mysl.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mysl.api.entity.ClassCourse;
import com.mysl.api.entity.dto.CourseDTO;
import com.mysl.api.entity.dto.MediaDTO;
import com.mysl.api.entity.enums.ClassCourseStatus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/15
 */
public interface ClassCourseMapper extends BaseMapper<ClassCourse> {

    List<CourseDTO> findAll(@Param("class_id") Long classId);

    List<MediaDTO> findMediaByClassIdAndCourseId(@Param("class_id") Long classId, @Param("course_id") Long courseId);

    int updateClassCourseStatus(@Param("class_id") Long classId, @Param("course_id") Long courseId,
                                @Param("status")ClassCourseStatus status, @Param("updated_by") String updatedBy);
}
