package com.mysl.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysl.api.entity.Course;
import com.mysl.api.entity.CourseMedia;
import com.mysl.api.entity.dto.CourseCreateDTO;
import com.mysl.api.mapper.CourseMapper;
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
    MediaMapper mediaMapper;

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

}
