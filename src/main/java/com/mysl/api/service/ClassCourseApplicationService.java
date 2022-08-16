package com.mysl.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mysl.api.entity.ClassCourseApplication;
import com.mysl.api.entity.dto.ClassCourseApplicationDTO;

/**
 * @author Ivan Su
 * @date 2022/8/14
 */
public interface ClassCourseApplicationService extends IService<ClassCourseApplication> {

    PageInfo<ClassCourseApplicationDTO> getApplications(Integer pageNum, Integer pageSize);

    boolean audit(Long id, Boolean result);
}
