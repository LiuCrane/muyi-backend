package com.mysl.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysl.api.entity.CourseMedia;
import com.mysl.api.mapper.CourseMediaMapper;
import com.mysl.api.service.CourseMediaService;
import org.springframework.stereotype.Service;

/**
 * @author Ivan Su
 * @date 2022/8/14
 */
@Service
public class CourseMediaServiceImpl extends ServiceImpl<CourseMediaMapper, CourseMedia> implements CourseMediaService {
}
