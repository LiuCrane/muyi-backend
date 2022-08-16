package com.mysl.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysl.api.common.exception.ResourceNotFoundException;
import com.mysl.api.config.security.JwtTokenUtil;
import com.mysl.api.entity.*;
import com.mysl.api.entity.Class;
import com.mysl.api.entity.dto.MediaDTO;
import com.mysl.api.entity.dto.PlayerEventDTO;
import com.mysl.api.entity.enums.ClassCourseStatus;
import com.mysl.api.entity.enums.MediaType;
import com.mysl.api.entity.enums.PlayerEvent;
import com.mysl.api.entity.enums.StudyProgress;
import com.mysl.api.mapper.*;
import com.mysl.api.service.MediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/13
 */
@Service
@Slf4j
public class MediaServiceImpl extends ServiceImpl<MediaMapper, Media> implements MediaService {

    @Autowired
    ClassCourseMapper classCourseMapper;
    @Autowired
    ClassMapper classMapper;
    @Autowired
    ClassCourseMediaEventMapper eventMapper;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    StudentMapper studentMapper;

    @Override
    public PageInfo<MediaDTO> getMediaList(Integer pageNum, Integer pageSize, Long id, MediaType type, Boolean publicly) {
        PageHelper.startPage(pageNum, pageSize);
        List<MediaDTO> list = super.baseMapper.findAll(id, type, publicly);
        return new PageInfo<>(list);
    }

    @Override
    public boolean savePlayerEvent(Long id, PlayerEventDTO dto, Long storeId) {
        if (dto.getClassId() == null || dto.getCourseId() == null) {
            return true;
        }
        Class cls = classMapper.selectOne(new QueryWrapper<Class>().eq("store_id", storeId).eq("id", dto.getClassId()));
        if (cls == null) {
            throw new ResourceNotFoundException("找不到班级");
        }
        ClassCourse classCourse = classCourseMapper.selectOne(new QueryWrapper<ClassCourse>()
                .eq("class_id", dto.getClassId()).eq("course_id", dto.getCourseId()));
        if (classCourse == null) {
            throw new ResourceNotFoundException("找不到课程");
        }
        // 保存记录
        ClassCourseMediaEvent event = ClassCourseMediaEvent.builder()
                .storeId(storeId).classCourseId(classCourse.getId())
                .mediaId(id).playerEvent(dto.getEvent()).build();
        eventMapper.insert(event);

        // 处理课程学习进度
        handleCauseProgress(dto.getClassId(), dto.getCourseId(), dto.getEvent());
        return true;
    }

    @Async
    @Transactional
    private void handleCauseProgress(Long classId, Long courseId, PlayerEvent event) {
        switch (event) {
            case START:
                // 修改班级学习进度
                Class cls1 = classMapper.selectById(classId);
                if (StudyProgress.NOT_STARTED.equals(cls1.getStudyProgress())) {
                    cls1.setStudyProgress(StudyProgress.IN_PROGRESS);
                    classMapper.updateById(cls1);
                }
                break;
            case END:
                // 判断课程内媒体是否播放完
                if (super.baseMapper.countUnfinishedMedia(classId, courseId) == 0) {
                    // 媒体都播放完
                    // 课程状态改为已完成
                    classCourseMapper.updateClassCourseStatus(classId, courseId, ClassCourseStatus.COMPLETED, JwtTokenUtil.getCurrentUsername());
                    Course course = courseMapper.selectById(courseId);
                    Course nextCourse = courseMapper.selectOne(new QueryWrapper<Course>()
                            .eq("active", 1).gt("created_at", course.getCreatedAt())
                            .orderByAsc("created_at").last("limit 1"));
                    if (nextCourse != null) {
                        // 找到下一个课程，标为可申请
                        ClassCourse classCourse = classCourseMapper.selectOne(
                                new QueryWrapper<ClassCourse>().eq("class_id", classId).eq("course_id", nextCourse.getId())
                        );
                        if (classCourse == null) {
                            classCourse = ClassCourse.builder().classId(classId).courseId(nextCourse.getId())
                                    .status(ClassCourseStatus.APPLICABLE).build();
                            classCourseMapper.insert(classCourse);
                        } else {
                            classCourse.setStatus(ClassCourseStatus.APPLICABLE);
                            classCourseMapper.updateById(classCourse);
                        }
                    } else {
                        // 找不到下个课程，则该班级学员学习进度进入复训
                        Class cls2 = classMapper.selectById(classId);
                        if (StudyProgress.IN_PROGRESS.equals(cls2.getStudyProgress())) {
                            cls2.setStudyProgress(StudyProgress.REHAB_TRAINING);
                            classMapper.updateById(cls2);
                            studentMapper.updateRehabByClassId(classId, JwtTokenUtil.getCurrentUsername());
                        }
                    }
                }

                break;
        }
    }
}
