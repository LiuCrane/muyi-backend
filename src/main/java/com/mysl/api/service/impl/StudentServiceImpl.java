package com.mysl.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysl.api.entity.Student;
import com.mysl.api.mapper.StudentMapper;
import com.mysl.api.service.StudentService;
import org.springframework.stereotype.Service;

/**
 * @author Ivan Su
 * @date 2022/8/13
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
}
