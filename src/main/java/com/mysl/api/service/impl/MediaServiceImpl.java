package com.mysl.api.service.impl;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.WeakCache;
import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysl.api.common.exception.ResourceNotFoundException;
import com.mysl.api.common.exception.ServiceException;
import com.mysl.api.config.security.JwtTokenUtil;
import com.mysl.api.entity.*;
import com.mysl.api.entity.Class;
import com.mysl.api.entity.dto.*;
import com.mysl.api.entity.enums.ClassCourseStatus;
import com.mysl.api.entity.enums.MediaType;
import com.mysl.api.entity.enums.PlayerEvent;
import com.mysl.api.entity.enums.StudyProgress;
import com.mysl.api.mapper.*;
import com.mysl.api.service.MediaBrowseRecordService;
import com.mysl.api.service.MediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    StoreMapper storeMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    CourseMediaMapper courseMediaMapper;
    @Autowired
    MediaBrowseRecordService browseRecordService;

    @Override
    public PageInfo<MediaDTO> getMediaList(Integer pageNum, Integer pageSize, Long id, MediaType type, Boolean publicly) {
        PageHelper.startPage(pageNum, pageSize);
        List<MediaFullDTO> list = super.baseMapper.findAll(id, type, publicly, null, null, null, null);
        PageInfo<MediaDTO> pageInfo = new PageInfo<>();
        CglibUtil.copy(new PageInfo<>(list), pageInfo);
        pageInfo.setList(CglibUtil.copyList(list, MediaDTO::new));
        return pageInfo;
    }

    @Override
    public PageInfo<MediaFullDTO> getMediaList(Integer pageNum, Integer pageSize, String keyWord) {
        PageHelper.startPage(pageNum, pageSize);
        List<MediaFullDTO> list = super.baseMapper.findAll(null, null, null, null, null, null, keyWord);
        return new PageInfo<>(list);
    }

    @Override
    public MediaFullDTO getMediaById(Long id) {
        List<MediaFullDTO> list = super.baseMapper.findAll(id, null, null, null, null, null, null);
        if (CollectionUtils.isEmpty(list)) {
            throw new ResourceNotFoundException("找不到媒体");
        }
        return list.get(0);
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

        // 保存浏览记录
        saveBrowseRecord(storeId, dto.getClassId(), dto.getCourseId(), id, dto.getEvent());
        return true;
    }

    @Override
    public boolean remove(Long id) {
        Media media = super.getById(id);
        if (media == null) {
            throw new ResourceNotFoundException("找不到媒体");
        }
        media.setActive(Boolean.FALSE);
        super.updateById(media);
        return true;
    }

    WeakCache<String, List<MediaCategoryDTO>> categoriesCache = CacheUtil.newWeakCache(DateUnit.SECOND.getMillis() * 10);
    private static final String CATEGORIES_CACHE_KEY = "categories";
    @Override
    public List<MediaCategoryDTO> getCategories() {
        List<MediaCategoryDTO> list = categoriesCache.get(CATEGORIES_CACHE_KEY);
        if (CollectionUtils.isEmpty(list)) {
            list = new ArrayList<>();
            List<Course> courses = courseMapper.selectList(new QueryWrapper<Course>()
                    .select("id", "title").eq("active", 1)
                    .orderByAsc("created_at"));
            for (Course c : courses) {
                list.add(new MediaCategoryDTO().setId(c.getId()).setName(c.getTitle()));
            }
            categoriesCache.put(CATEGORIES_CACHE_KEY, list);
        }
        return list;
    }

    @Override
    @Transactional
    public boolean save(MediaEditDTO dto) {
        Course course = courseMapper.selectById(dto.getCategoryId());
        if (course == null) {
            throw new ServiceException("媒体分类不存在");
        }
        Media media = new Media();
        BeanUtils.copyProperties(dto, media);
        media.setDuration(dto.getDurationActual());
        media.setCourseId(dto.getCategoryId());
        if (!super.save(media)) {
            throw new ServiceException("操作失败");
        }

        CourseMedia courseMedia = CourseMedia.builder().courseId(course.getId()).mediaId(media.getId()).build();
        if (courseMediaMapper.insert(courseMedia) < 1) {
            throw new ServiceException("操作失败");
        }

        // 更新课程时长
        updateCourseDuration(course);

        return true;
    }

    @Override
    @Transactional
    public boolean update(Long id, MediaEditDTO dto) {
        Course course = courseMapper.selectById(dto.getCategoryId());
        if (course == null) {
            throw new ServiceException("媒体分类不存在");
        }
        Media media = super.getById(id);
        if (media == null) {
            throw new ResourceNotFoundException("找不到媒体");
        }
        Long oldCourseId = media.getCourseId();
        BeanUtils.copyProperties(dto, media);
        media.setDuration(dto.getDurationActual());
        media.setCourseId(dto.getCategoryId());
        if (!super.updateById(media)) {
            throw new ServiceException("操作失败");
        }
        if (!oldCourseId.equals(dto.getCategoryId())) {
            CourseMedia courseMedia = CourseMedia.builder().courseId(course.getId()).mediaId(media.getId()).build();
            if (courseMediaMapper.insert(courseMedia) < 1) {
                throw new ServiceException("操作失败");
            }
            // 更新课程时长
            updateCourseDuration(course);

            if (oldCourseId > 0) {
                // 移除原来的关联
                if (courseMediaMapper.deleteOne(oldCourseId, id) < 1) {
                    throw new ServiceException("操作失败");
                }
                // 更新旧课程时长
                Course oldCourse = courseMapper.selectById(oldCourseId);
                updateCourseDuration(oldCourse);
            }

        }
        return true;
    }

    private void updateCourseDuration(Course course) {
        List<Long> mediaIds = courseMediaMapper.findMediaIds(course.getId());
        int duration = super.baseMapper.sumMediaDuration(mediaIds);
        course.setDuration(BigDecimal.valueOf(duration));
        if (courseMapper.updateById(course) < 1) {
            throw new ServiceException("操作失败");
        }
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

    @Async
    @Transactional
    private void saveBrowseRecord(Long storeId, Long classId, Long courseId, Long mediaId, PlayerEvent event) {
        if (!PlayerEvent.END.equals(event)) {
            return;
        }
        Store store = storeMapper.selectById(storeId);
        if (store == null) {
            return;
        }
        User manger = userMapper.selectById(store.getManagerUserId());
        if (manger == null) {
            return;
        }
        Class cls = classMapper.selectById(classId);
        if (cls == null) {
            return;
        }
        List<Student> students = studentMapper.selectList(new QueryWrapper<Student>().eq("class_id", classId));
        if (CollectionUtils.isEmpty(students)) {
            return;
        }
        String stuIds = students.stream().map(m -> m.getId().toString()).collect(Collectors.joining(","));

        Course course = courseMapper.selectById(courseId);
        if (course == null) {
            return;
        }
        Media media = super.getById(mediaId);
        if (media == null) {
            return;
        }
        ClassCourse classCourse = classCourseMapper.selectOne(new QueryWrapper<ClassCourse>()
                .eq("class_id", classId).eq("course_id", courseId));
        if (classCourse == null) {
            return;
        }
        ClassCourseMediaEvent firstStartEvent = eventMapper.getEvent(classCourse.getId(), mediaId, PlayerEvent.START, null);
        if (firstStartEvent == null) {
            return;
        }
        ClassCourseMediaEvent lastEndEvent = eventMapper.getEvent(classCourse.getId(), mediaId, PlayerEvent.END, Boolean.TRUE);
        if (lastEndEvent == null) {
            return;
        }
        String totalTime = DateUtil.formatBetween(firstStartEvent.getCreatedAt(), lastEndEvent.getCreatedAt(), BetweenFormatter.Level.SECOND);

        MediaBrowseRecord browseRecord = MediaBrowseRecord.builder()
                .userId(store.getManagerUserId())
                .storeId(store.getId()).storeName(store.getName())
                .storeAddress(store.getAddress())
                .storeManager(manger.getName()).storeManagerPhone(manger.getPhone())
                .classId(cls.getId()).className(cls.getName())
                .studentIds(stuIds)
                .courseId(courseId).courseTitle(course.getTitle())
                .mediaId(mediaId).mediaTitle(media.getTitle())
                .mediaType(media.getType().name())
                .mediaDescription(media.getDescription())
                .startTime(firstStartEvent.getCreatedAt())
                .endTime(lastEndEvent.getCreatedAt())
                .totalTime(totalTime)
                .build();
        browseRecordService.save(browseRecord);

        eventMapper.updateRecorded(classCourse.getId(), mediaId);

    }
}
