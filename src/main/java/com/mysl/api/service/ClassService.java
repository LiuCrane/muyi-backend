package com.mysl.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mysl.api.entity.Class;
import com.mysl.api.entity.dto.ClassDTO;
import com.mysl.api.entity.dto.ClassFullDTO;
import com.mysl.api.entity.dto.CourseDTO;
import com.mysl.api.entity.dto.MediaDTO;

import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/13
 */
public interface ClassService extends IService<Class> {

    List<ClassFullDTO> getClasses(Integer pageNum, Integer pageSize, Long id, Long storeId);

    PageInfo<ClassDTO> getClasses(Integer pageNum, Integer pageSize, Long storeId);

    List<CourseDTO> getClassCourse(Long storeId, Long classId);

    boolean applyCourse(Long storeId, Long classId, Long courseId);

    List<MediaDTO> getClassCourseMedia(Long storeId, Long classId, Long courseId);
}
