package com.mysl.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mysl.api.entity.ClassCourseApplication;
import com.mysl.api.entity.dto.ClassCourseApplicationDTO;

import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/14
 */
public interface ClassCourseApplicationMapper extends BaseMapper<ClassCourseApplication> {

    List<ClassCourseApplicationDTO> findAll();
}
