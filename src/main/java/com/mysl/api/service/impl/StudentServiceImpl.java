package com.mysl.api.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysl.api.common.exception.ResourceNotFoundException;
import com.mysl.api.common.exception.ServiceException;
import com.mysl.api.config.security.JwtTokenUtil;
import com.mysl.api.entity.*;
import com.mysl.api.entity.dto.*;
import com.mysl.api.entity.enums.ClassCourseStatus;
import com.mysl.api.mapper.*;
import com.mysl.api.service.AddressService;
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
    @Autowired
    AddressInfoMapper addressInfoMapper;
    @Autowired
    AddressService addressService;
    @Autowired
    ClassCourseMapper classCourseMapper;
    @Autowired
    CourseMapper courseMapper;

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
        List<StudentEyesightDTO> eyesightDTOS = getStudentEyesight(id);
        eyesightDTOS.add(0, StudentEyesightDTO.builder().title("疗愈前")
                .leftVision(dto.getFirstLeftVision()).rightVision(dto.getFirstRightVision())
                .binocularVision(dto.getBinocularVision()).createdAt(dto.getCreatedAt()).build());
        dto.setEyesightList(eyesightDTOS);
        return dto;
    }

    @Override
    public StudentFullDTO getStudentByStoreIdAndId(Long storeId, Long id) {
        List<StudentFullDTO> list = super.baseMapper.findAll(id, null, storeId, null, null, null);
        if (CollectionUtils.isEmpty(list)) {
            throw new ResourceNotFoundException("找不到学员信息");
        }
        StudentFullDTO dto = list.get(0);
        List<StudentEyesightDTO> eyesightDTOS = getStudentEyesight(id);
        eyesightDTOS.add(0, StudentEyesightDTO.builder().title("疗愈前")
                .leftVision(dto.getFirstLeftVision()).rightVision(dto.getFirstRightVision())
                .binocularVision(dto.getBinocularVision()).createdAt(dto.getCreatedAt()).build());
        dto.setEyesightList(eyesightDTOS);
        return dto;
    }

    private List<StudentEyesightDTO> getStudentEyesight(Long studentId) {
        return studentEyesightMapper.findByStudentId(studentId);
    }

    @Override
    @Transactional
    public boolean save(StudentCreateDTO dto) {
        Student student = new Student();
        BeanUtils.copyProperties(dto, student);
        student.setStoreId(JwtTokenUtil.getCurrentStoreId());

        if (dto.getAreaId() != null) {
            Address address = addressService.getById(dto.getAreaId());
            if (address == null) {
                throw new ServiceException("所选地区不存在");
            }
            AddressCascadeDTO addressCascadeDTO = addressService.getAddressCascade(dto.getAreaId());
            AddressInfo info = AddressInfo.builder().lastAreaId(dto.getAreaId())
                    .addressCascade(JSON.toJSONString(addressCascadeDTO))
                    .addressArea(addressService.getAreaByCascade(addressCascadeDTO))
                    .addressDetail(dto.getAddressDetail()).build();
            if (addressInfoMapper.insert(info) < 1) {
                throw new ServiceException("操作失败");
            }
            student.setAddressInfoId(info.getId());
        }

        if (!super.save(student)) {
            throw new ServiceException("操作失败");
//            StudentEyesight eyesight = StudentEyesight.builder()
//                    .studentId(student.getId())
//                    .leftDiopter(dto.getLeftDiopter())
//                    .rightDiopter(dto.getRightDiopter())
//                    .leftVision(dto.getLeftVision())
//                    .rightVision(dto.getRightVision())
//                    .build();
//            return studentEyesightMapper.insert(eyesight) > 0;
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
        ClassCourse classCourse = classCourseMapper.selectOne(new QueryWrapper<ClassCourse>().eq("class_id", student.getClassId()).eq("course_id", dto.getCourseId()));
        if (classCourse == null) {
            throw new ResourceNotFoundException("找不到课程");
        }
        if (!ClassCourseStatus.COMPLETED.equals(classCourse.getStatus())) {
            throw new ServiceException("该课程未学习完成");
        }

        String lastBinocularVision = student.getBinocularVision();
        StudentEyesight last = studentEyesightMapper.selectOne(
                new QueryWrapper<StudentEyesight>().eq("student_id", id).orderByDesc("created_at").last("limit 1"));
        if (last != null) {
            lastBinocularVision = last.getBinocularVision();
        }
        Boolean improved = null;
        if (NumberUtil.toBigDecimal(dto.getBinocularVision())
                .compareTo(NumberUtil.toBigDecimal(lastBinocularVision)) > 0) {
            improved = Boolean.TRUE;
        } else {
            improved = Boolean.FALSE;
        }
        student.setImproved(improved);
        if (super.updateById(student)) {
            StudentEyesight eyesight = studentEyesightMapper.selectOne(new QueryWrapper<StudentEyesight>()
                    .eq("student_id", id).eq("course_id", dto.getCourseId()));
            if (eyesight != null) {
                eyesight.setLeftVision(dto.getLeftVision()).setRightVision(dto.getRightVision())
                        .setBinocularVision(dto.getBinocularVision()).setImproved(improved);
                return studentEyesightMapper.updateById(eyesight) > 0;
            } else {
                eyesight = StudentEyesight.builder().studentId(id).courseId(dto.getCourseId())
                        .leftVision(dto.getLeftVision()).rightVision(dto.getRightVision())
                        .binocularVision(dto.getBinocularVision()).improved(improved).build();
                return studentEyesightMapper.insert(eyesight) > 0;
            }
        }
        return false;
    }
}
