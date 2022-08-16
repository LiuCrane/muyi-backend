package com.mysl.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.mysl.api.common.exception.ResourceNotFoundException;
import com.mysl.api.entity.Class;
import com.mysl.api.entity.ClassCourse;
import com.mysl.api.entity.Course;
import com.mysl.api.entity.dto.ClassFullDTO;
import com.mysl.api.entity.dto.CourseDTO;
import com.mysl.api.entity.enums.ClassCourseStatus;
import com.mysl.api.entity.enums.StudyProgress;
import com.mysl.api.mapper.ClassCourseMapper;
import com.mysl.api.mapper.ClassMapper;
import com.mysl.api.mapper.CourseMapper;
import com.mysl.api.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/13
 */
@Service
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements ClassService {

    @Autowired
    ClassCourseMapper classCourseMapper;
    @Autowired
    CourseMapper courseMapper;

    @Override
    public List<ClassFullDTO> getClasses(Integer pageNum, Integer pageSize, Long id, Long storeId) {
        PageHelper.startPage(pageNum, pageSize);
        return super.baseMapper.findAll(id, storeId);
    }

    @Override
    public List<CourseDTO> getClassCourse(Long storeId, Long classId) {
        Class cls = super.baseMapper.selectOne(new QueryWrapper<Class>().eq("store_id", storeId).eq("id", classId));
        if (cls == null) {
            throw new ResourceNotFoundException("找不到班级信息");
        }

        if (StudyProgress.NOT_STARTED.equals(cls.getStudyProgress()) || StudyProgress.IN_PROGRESS.equals(cls.getStudyProgress())) {
            List<Course> courses = courseMapper.selectList(new QueryWrapper<Course>().eq("active", 1).orderByAsc("created_at"));
            if (CollectionUtils.isEmpty(courses)) {
                return new ArrayList<>();
            }
            List<ClassCourse> classCourses = classCourseMapper.selectList(new QueryWrapper<ClassCourse>().eq("class_id", classId).orderByDesc("created_at"));
            if (CollectionUtils.isEmpty(classCourses)) {
                // 当前无班级课程，创建一条可申请的课程
                ClassCourse classCourse = ClassCourse.builder()
                        .courseId(courses.get(0).getId()).classId(classId).status(ClassCourseStatus.APPLICABLE).build();
                classCourseMapper.insert(classCourse);
            }
        }
        return classCourseMapper.findAll(classId);
    }

    @Override
    public boolean applyCourse(Long storeId, Long classId, Long classCourseId) {
        Class cls = super.baseMapper.selectOne(new QueryWrapper<Class>().eq("store_id", storeId).eq("id", classId));
        if (cls == null) {
            throw new ResourceNotFoundException("找不到班级信息");
        }
        ClassCourse classCourse = classCourseMapper.selectOne(new QueryWrapper<ClassCourse>().eq("id", classCourseId).eq("class_id", classId));
        if (classCourse == null) {
            throw new ResourceNotFoundException("找不到课程信息");
        }
        return false;
    }
}
