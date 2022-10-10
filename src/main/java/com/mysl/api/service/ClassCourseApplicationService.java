package com.mysl.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mysl.api.entity.ClassCourseApplication;
import com.mysl.api.entity.dto.ApplicationDTO;
import com.mysl.api.entity.dto.ClassCourseApplicationDTO;

import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/14
 */
public interface ClassCourseApplicationService extends IService<ClassCourseApplication> {

    PageInfo<ClassCourseApplicationDTO> getApplications(Integer pageNum, Integer pageSize);

    PageInfo<ApplicationDTO> getApplications(Integer pageNum, Integer pageSize, String keyWord);

    boolean audit(List<Long> ids, Boolean result);
}
