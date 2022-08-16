package com.mysl.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysl.api.common.exception.ServiceException;
import com.mysl.api.entity.ClassCourse;
import com.mysl.api.entity.ClassCourseApplication;
import com.mysl.api.entity.dto.ClassCourseApplicationDTO;
import com.mysl.api.entity.enums.ClassCourseStatus;
import com.mysl.api.mapper.ClassCourseApplicationMapper;
import com.mysl.api.mapper.ClassCourseMapper;
import com.mysl.api.service.ClassCourseApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Ivan Su
 * @date 2022/8/16
 */
@Service
public class ClassCourseApplicationServiceImpl extends ServiceImpl<ClassCourseApplicationMapper, ClassCourseApplication>
        implements ClassCourseApplicationService {

    @Autowired
    ClassCourseMapper classCourseMapper;

    @Override
    public PageInfo<ClassCourseApplicationDTO> getApplications(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(super.baseMapper.findAll());
    }

    @Override
    @Transactional
    public boolean audit(Long id, Boolean result) {
        ClassCourseApplication application = super.baseMapper.selectById(id);
        if (application == null) {
            throw new ServiceException("找不到申请信息");
        }
        if (application.getResult() != null) {
            throw new ServiceException("该申请已审核");
        }
        application.setResult(result);
        if (super.updateById(application)) {
            ClassCourse classCourse = classCourseMapper.selectById(application.getClassCourseId());
            if (Boolean.TRUE.equals(result)) {
                classCourse.setStatus(ClassCourseStatus.ACCESSIBLE);
            } else {
                classCourse.setStatus(ClassCourseStatus.APPLICABLE);
            }
            return classCourseMapper.updateById(classCourse) > 0;
        }
        return false;
    }
}
