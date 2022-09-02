package com.mysl.api.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysl.api.common.exception.ResourceNotFoundException;
import com.mysl.api.config.security.JwtTokenUtil;
import com.mysl.api.entity.Student;
import com.mysl.api.entity.StudentEyesight;
import com.mysl.api.entity.dto.*;
import com.mysl.api.mapper.StudentEyesightMapper;
import com.mysl.api.mapper.StudentMapper;
import com.mysl.api.service.StudentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/13
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Autowired
    StudentEyesightMapper studentEyesightMapper;

    @Override
    public PageInfo<StudentFullDTO> getStudents(Integer pageNum, Integer pageSize, Long id, String name,
                                            Long storeId, Long classId, Boolean rehab, String keyWord) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(super.baseMapper.findAll(id, name, storeId, classId, rehab, keyWord));
    }

    @Override
    public PageInfo<StudentDTO> getStudents(Integer pageNum, Integer pageSize, Long storeId, Long classId, Boolean rehab) {
        PageHelper.startPage(pageNum, pageSize);
        List<StudentFullDTO> list = super.baseMapper.findAll(null, null, storeId, classId, rehab, null);
        PageInfo<StudentDTO> pageInfo = new PageInfo<>();
        CglibUtil.copy(new PageInfo<>(list), pageInfo);
        pageInfo.setList(CglibUtil.copyList(list, StudentDTO::new));
        return pageInfo;
    }

    @Override
    public List<StudentSimpleDTO> getSimpleStudents(Long storeId, Long classId) {
        return super.baseMapper.findSimpleList(storeId, classId);
    }

    @Override
    public StudentFullDTO getStudentById(Long id) {
        List<StudentFullDTO> list = super.baseMapper.findAll(id, null, null, null, null, null);
        if (CollectionUtils.isEmpty(list)) {
            throw new ResourceNotFoundException("找不到学员信息");
        }
        StudentFullDTO dto = list.get(0);
        dto.setEyesightList(getStudentEyesight(id));
        return dto;
    }

    @Override
    public StudentFullDTO getStudentByStoreIdAndId(Long storeId, Long id) {
        List<StudentFullDTO> list = super.baseMapper.findAll(id, null, storeId, null, null, null);
        if (CollectionUtils.isEmpty(list)) {
            throw new ResourceNotFoundException("找不到学员信息");
        }
        StudentFullDTO dto = list.get(0);
        dto.setEyesightList(getStudentEyesight(id));
        return dto;
    }

    private List<StudentEyesightDTO> getStudentEyesight(Long studentId) {
        List<StudentEyesightDTO> ret = new ArrayList<>();
        List<StudentEyesight> list = studentEyesightMapper.selectList(new QueryWrapper<StudentEyesight>().eq("student_id", studentId).orderByAsc("id"));
        if (!CollectionUtils.isEmpty(list)) {
            ret = CglibUtil.copyList(list, StudentEyesightDTO::new);
        }
        return ret;
    }

    @Override
    @Transactional
    public boolean save(StudentCreateDTO dto) {
        Student student = new Student();
        BeanUtils.copyProperties(dto, student);
        student.setStoreId(JwtTokenUtil.getCurrentStoreId());
        if (super.save(student)) {
            StudentEyesight eyesight = StudentEyesight.builder()
                    .studentId(student.getId())
                    .leftDiopter(dto.getLeftDiopter())
                    .rightDiopter(dto.getRightDiopter())
                    .leftVision(dto.getLeftVision())
                    .rightVision(dto.getRightVision())
                    .build();
            return studentEyesightMapper.insert(eyesight) > 0;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean updateVision(Long storeId, Long id, StudentVisionDTO dto) {
        Student student = super.baseMapper.selectOne(new QueryWrapper<Student>().eq("store_id", storeId).eq("id", id));
        if (student == null) {
            throw new ResourceNotFoundException("找不到学员信息");
        }
        StudentEyesight last = studentEyesightMapper.selectList(
                new QueryWrapper<StudentEyesight>().eq("student_id", id).orderByDesc("created_at")).get(0);
        Boolean improved = Boolean.FALSE;
        if (NumberUtil.add(dto.getLeftVision(), dto.getRightVision())
                .compareTo(NumberUtil.add(last.getLeftVision(), last.getRightVision())) > 0) {
            improved = Boolean.TRUE;
        }
        student.setLeftVision(dto.getLeftVision());
        student.setRightVision(dto.getRightVision());
        student.setImproved(improved);
        if (super.updateById(student)) {
            StudentEyesight eyesight = StudentEyesight.builder().studentId(id)
                    .leftVision(dto.getLeftVision()).rightVision(dto.getRightVision()).improved(improved).build();
            return studentEyesightMapper.insert(eyesight) > 0;
        }
        return false;
    }
}
