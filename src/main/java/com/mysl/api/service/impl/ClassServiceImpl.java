package com.mysl.api.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysl.api.common.GlobalConstant;
import com.mysl.api.common.exception.ResourceNotFoundException;
import com.mysl.api.common.exception.ServiceException;
import com.mysl.api.common.lang.ResponseData;
import com.mysl.api.common.util.CosUtil;
import com.mysl.api.config.WebSocketServer;
import com.mysl.api.entity.Class;
import com.mysl.api.entity.ClassCourse;
import com.mysl.api.entity.ClassCourseApplication;
import com.mysl.api.entity.Course;
import com.mysl.api.entity.dto.*;
import com.mysl.api.entity.enums.ClassCourseStatus;
import com.mysl.api.entity.enums.CourseType;
import com.mysl.api.entity.enums.StudyProgress;
import com.mysl.api.mapper.ClassCourseApplicationMapper;
import com.mysl.api.mapper.ClassCourseMapper;
import com.mysl.api.mapper.ClassMapper;
import com.mysl.api.mapper.CourseMapper;
import com.mysl.api.service.ApplicationService;
import com.mysl.api.service.ClassService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/13
 */
@Service
@Slf4j
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements ClassService {

    @Autowired
    ClassCourseMapper classCourseMapper;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    ClassCourseApplicationMapper classCourseApplicationMapper;
    @Autowired
    ApplicationService applicationService;

    @Override
    public PageInfo<ClassFullDTO> getClasses(Integer pageNum, Integer pageSize, Long id, Long storeId, String keyWord) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(super.baseMapper.findAll(id, storeId, keyWord));
    }

    @Override
    public PageInfo<ClassDTO> getClasses(Integer pageNum, Integer pageSize, Long storeId) {
        PageHelper.startPage(pageNum, pageSize);
        List<ClassFullDTO> classes = super.baseMapper.findAll(null, storeId, null);
        PageInfo<ClassDTO> pageInfo = new PageInfo<>();
        CglibUtil.copy(new PageInfo<>(classes), pageInfo);
        pageInfo.setList(CglibUtil.copyList(classes, ClassDTO::new));
        return pageInfo;
    }

    @Override
    public ClassFullDTO getClassById(Long id, Long storeId) {
        List<ClassFullDTO> list = super.baseMapper.findAll(id, storeId, null);
        if (CollectionUtils.isEmpty(list)) {
            throw new ResourceNotFoundException("找不到班级");
        }
        return list.get(0);
    }

    @Override
    public List<CourseDTO> getClassCourse(Long storeId, Long classId) {
        Class cls = super.baseMapper.selectOne(new QueryWrapper<Class>().eq("store_id", storeId).eq("id", classId));
        if (cls == null) {
            throw new ResourceNotFoundException("找不到班级信息");
        }

        if (StudyProgress.NOT_STARTED.equals(cls.getStudyProgress()) || StudyProgress.IN_PROGRESS.equals(cls.getStudyProgress())) {
            List<Course> courses = courseMapper.selectList(new QueryWrapper<Course>()
                    .eq("active", 1).eq("type", CourseType.STAGE).orderByAsc("id"));
            if (CollectionUtils.isEmpty(courses)) {
                return new ArrayList<>();
            }
            List<ClassCourse> classCourses = classCourseMapper.selectList(new QueryWrapper<ClassCourse>()
                    .eq("class_id", classId).orderByDesc("created_at"));
            if (CollectionUtils.isEmpty(classCourses)) {
                // 当前无班级课程，创建一条可申请的课程
                ClassCourse classCourse = ClassCourse.builder()
                        .courseId(courses.get(0).getId()).classId(classId).status(ClassCourseStatus.APPLICABLE).build();
                classCourseMapper.insert(classCourse);
            } else {
                ClassCourse lastClassCourse = classCourses.get(0);
                if (ClassCourseStatus.EXPIRED.equals(lastClassCourse.getStatus())) {
                    Course nextCourse = courseMapper.selectOne(new QueryWrapper<Course>()
                            .eq("active", 1).eq("type", CourseType.STAGE).gt("id", lastClassCourse.getCourseId())
                            .orderByAsc("id").last("limit 1"));
                    ClassCourse classCourse = ClassCourse.builder()
                            .courseId(nextCourse.getId()).classId(classId).status(ClassCourseStatus.APPLICABLE).build();
                    classCourseMapper.insert(classCourse);
                }
            }
        }
        List<CourseDTO> list = classCourseMapper.findAll(classId);
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(c -> {
                if (StringUtils.isNotEmpty(c.getCoverUrl())) {
                    c.setCoverUrl(CosUtil.getImgShowUrl(c.getCoverUrl()));
                }
            });
        }
        return list;
    }

    @Override
    public ClassCourseStatus getClassCourseStatus(Long storeId, Long classId, Long courseId) {
        String cacheKey = String.format(GlobalConstant.courseStatusCacheKeyFormat, classId, courseId);
        ClassCourseStatus status = GlobalConstant.courseStatusCache.get(cacheKey);
        if (status != null) {
            return status;
        }
        Class cls = super.baseMapper.selectOne(new QueryWrapper<Class>().eq("store_id", storeId).eq("id", classId));
        if (cls == null) {
            throw new ResourceNotFoundException("找不到班级信息");
        }
        ClassCourse classCourse = classCourseMapper.selectOne(new QueryWrapper<ClassCourse>()
                .eq("class_id", classId).eq("course_id", courseId).last("limit 1"));
        if (classCourse != null) {
            GlobalConstant.courseStatusCache.put(cacheKey, classCourse.getStatus());
            return classCourse.getStatus();
        }
        return null;
    }

    @Override
    @Transactional
    public boolean applyCourse(Long storeId, Long classId, Long courseId) {
        Class cls = super.baseMapper.selectOne(new QueryWrapper<Class>().eq("store_id", storeId).eq("id", classId));
        if (cls == null) {
            throw new ResourceNotFoundException("找不到班级信息");
        }
        ClassCourse classCourse = classCourseMapper.selectOne(
                new QueryWrapper<ClassCourse>().eq("course_id", courseId).eq("class_id", classId));
        if (classCourse == null || !ClassCourseStatus.APPLICABLE.equals(classCourse.getStatus())) {
            throw new ServiceException("当前课程不可申请");
        }
        classCourse.setStatus(ClassCourseStatus.UNDER_APPLICATION);
        if (classCourseMapper.updateById(classCourse) > 0) {
            ClassCourseApplication application = new ClassCourseApplication();
            application.setClassCourseId(classCourse.getId());
            if (classCourseApplicationMapper.insert(application) > 0) {
                int count = applicationService.countApplications();
                WebSocketServer.send(JSON.toJSONString(ResponseData.ok(count)));

                String courseCacheKey = String.format(GlobalConstant.courseStatusCacheKeyFormat, classId, courseId);
                GlobalConstant.courseStatusCache.remove(courseCacheKey);
            } else {
                throw new ServiceException("操作失败");
            }
        }
        return false;
    }

    @Override
    public List<MediaDTO> getClassCourseMedia(Long storeId, Long classId, Long courseId) {
        Class cls = super.baseMapper.selectOne(new QueryWrapper<Class>().eq("store_id", storeId).eq("id", classId));
        if (cls == null) {
            throw new ResourceNotFoundException("找不到班级信息");
        }
        ClassCourse classCourse = classCourseMapper.selectOne(
                new QueryWrapper<ClassCourse>().eq("course_id", courseId).eq("class_id", classId));
        if (classCourse != null && ClassCourseStatus.ACCESSIBLE.equals(classCourse.getStatus())) {
            List<MediaDTO> list = classCourseMapper.findMediaByClassIdAndCourseId(classId, courseId);
            if (!CollectionUtils.isEmpty(list)) {
                Date expirationDate = DateUtil.endOfDay(new Date());
                list.forEach(m -> {
                    if (StringUtils.isNotEmpty(m.getImg())) {
                        m.setImg(CosUtil.getImgShowUrl(m.getImg()));
                    }
                    if (StringUtils.isNotEmpty(m.getUrl())) {
                        m.setUrl(CosUtil.generatePresignedDownloadUrl(m.getUrl(), expirationDate));
                    }
                });
                return list;
            }
        }
        return new ArrayList<>();
    }

    @Override
    public boolean update(Long id, ClassUpdateDTO dto) {
        Class cls = super.getById(id);
        if (cls == null) {
            throw new ResourceNotFoundException("找不到班级");
        }
        cls.setName(dto.getName());
        return super.updateById(cls);
    }

    @Override
    @Transactional
    public void expireClassCourse() {
        String updatedBy = "Schedule";
        classCourseMapper.updateClassCourseStatus(null, null, ClassCourseStatus.COMPLETED,
                updatedBy, ClassCourseStatus.ACCESSIBLE, Boolean.TRUE, Boolean.TRUE);
        classCourseMapper.updateClassCourseStatus(null, null, ClassCourseStatus.EXPIRED,
                updatedBy, ClassCourseStatus.ACCESSIBLE, Boolean.TRUE, Boolean.FALSE);
        GlobalConstant.courseStatusCache.clear();
    }

    @Override
    public void changeClassCourseStatus(Long classId, Long courseId, List<ClassCourseStatus> canChangeStatusList, ClassCourseStatus newStatus) {
        ClassCourse classCourse = classCourseMapper.selectOne(new QueryWrapper<ClassCourse>()
                .eq("class_id", classId).eq("course_id", courseId).last("limit 1"));
        if (classCourse == null) {
            throw new ResourceNotFoundException("找不到班级课程");
        }
        if (!canChangeStatusList.contains(classCourse.getStatus())) {
            throw new ServiceException("当前课程状态不可修改");
        }
        classCourse.setStatus(newStatus);
        classCourseMapper.updateById(classCourse);
        String courseCacheKey = String.format(GlobalConstant.courseStatusCacheKeyFormat, classId, courseId);
        GlobalConstant.courseStatusCache.remove(courseCacheKey);
    }

}
