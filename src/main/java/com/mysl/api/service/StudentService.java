package com.mysl.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mysl.api.entity.Student;
import com.mysl.api.entity.dto.StudentCreateDTO;
import com.mysl.api.entity.dto.StudentFullDTO;
import com.mysl.api.entity.dto.StudentSimpleDTO;
import com.mysl.api.entity.dto.StudentVisionDTO;

import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/13
 */
public interface StudentService extends IService<Student> {

    List<StudentFullDTO> getStudents(Integer pageNum, Integer pageSize, Long id, String name, Long storeId, Long classId, Boolean rehab);

    List<StudentSimpleDTO> getSimpleStudents(Long storeId, Long classId);

    StudentFullDTO getStudentById(Long id);

    StudentFullDTO getStudentByStoreIdAndId(Long storeId, Long id);

    boolean save(StudentCreateDTO dto);

    boolean updateVision(Long storeId, Long id, StudentVisionDTO dto);
}
