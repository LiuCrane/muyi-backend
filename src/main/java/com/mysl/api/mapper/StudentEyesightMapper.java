package com.mysl.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mysl.api.entity.StudentEyesight;
import com.mysl.api.entity.dto.StudentEyesightDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/15
 */
public interface StudentEyesightMapper extends BaseMapper<StudentEyesight> {

    List<StudentEyesightDTO> findByStudentId(@Param("student_id") Long studentId);
}
