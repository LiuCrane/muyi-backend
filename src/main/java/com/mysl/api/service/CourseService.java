package com.mysl.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mysl.api.entity.Course;
import com.mysl.api.entity.dto.CourseAuditDTO;
import com.mysl.api.entity.dto.CourseCreateDTO;

/**
 * @author Ivan Su
 * @date 2022/8/14
 */
public interface CourseService extends IService<Course> {

    boolean save(CourseCreateDTO dto);

    boolean update(Long id, CourseCreateDTO dto);

    boolean remove(Long id);
}
