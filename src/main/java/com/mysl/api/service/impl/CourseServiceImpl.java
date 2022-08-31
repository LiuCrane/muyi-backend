package com.mysl.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysl.api.common.exception.ResourceNotFoundException;
import com.mysl.api.common.exception.ServiceException;
import com.mysl.api.entity.Course;
import com.mysl.api.entity.CourseMedia;
import com.mysl.api.entity.dto.CourseCreateDTO;
import com.mysl.api.entity.enums.ClassCourseStatus;
import com.mysl.api.mapper.ClassCourseMapper;
import com.mysl.api.mapper.CourseMapper;
import com.mysl.api.mapper.CourseMediaMapper;
import com.mysl.api.mapper.MediaMapper;
import com.mysl.api.service.CourseMediaService;
import com.mysl.api.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ivan Su
 * @date 2022/8/14
 */
@Service
@Slf4j
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    CourseMediaService courseMediaService;
    @Autowired
    CourseMediaMapper courseMediaMapper;
    @Autowired
    MediaMapper mediaMapper;
    @Autowired
    ClassCourseMapper classCourseMapper;

    @Override
    @Transactional
    public boolean save(CourseCreateDTO dto) {
        log.info("save course: {}", dto);
        Course course = new Course();
        BeanUtils.copyProperties(dto, course);
        boolean mediaEmpty = CollectionUtils.isEmpty(dto.getMediaIds());
        if (!mediaEmpty) {
            int duration = mediaMapper.sumMediaDuration(dto.getMediaIds());
            course.setDuration(BigDecimal.valueOf(duration));
        }
        if (super.save(course) && !mediaEmpty) {
            List<CourseMedia> courseMediaList = dto.getMediaIds().stream()
                    .map(mediaId -> CourseMedia.builder().courseId(course.getId()).mediaId(mediaId).build())
                    .collect(Collectors.toList());
            return courseMediaService.saveBatch(courseMediaList);
        }
        return false;
    }

    @Override
    @Transactional
    public boolean update(Long id, CourseCreateDTO dto) {
        log.info("update course: {}", dto);
        Course course = super.getById(id);
        if (course == null) {
            throw new ResourceNotFoundException("找不到课程");
        }
        BeanUtils.copyProperties(dto, course);

        List<Long> oldMediaIds = courseMediaMapper.findMediaIds(id);
        if (!CollectionUtils.isEmpty(oldMediaIds)) {
            courseMediaMapper.deleteByCourseId(id);
        }

        if (!CollectionUtils.isEmpty(dto.getMediaIds())) {
            int duration = mediaMapper.sumMediaDuration(dto.getMediaIds());
            course.setDuration(BigDecimal.valueOf(duration));

            List<CourseMedia> courseMediaList = dto.getMediaIds().stream()
                    .map(mediaId -> CourseMedia.builder().courseId(course.getId()).mediaId(mediaId).build())
                    .collect(Collectors.toList());
            courseMediaService.saveBatch(courseMediaList);
        }

        if (!super.updateById(course)) {
            throw new ServiceException("修改失败");
        }

        return true;
    }

    @Override
    public boolean remove(Long id) {
        Course course = super.getById(id);
        if (course == null) {
            throw new ResourceNotFoundException("找不到课程");
        }
        int count = classCourseMapper.countByCourseIdAndStatus(id, ClassCourseStatus.ACCESSIBLE);
        if (count > 0) {
            throw new ServiceException("有学员正在学习该课程，无法删除");
        }
        course.setActive(Boolean.FALSE);
        return super.updateById(course);
    }


}
