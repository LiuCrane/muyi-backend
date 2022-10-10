package com.mysl.api.service.impl;

import com.mysl.api.mapper.ClassCourseApplicationMapper;
import com.mysl.api.mapper.StoreMapper;
import com.mysl.api.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Ivan Su
 * @date 2022/9/5
 */
@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    StoreMapper storeMapper;
    @Autowired
    ClassCourseApplicationMapper courseApplicationMapper;

    @Override
    public int countApplications() {
        int storeNum = storeMapper.countStores();
        int courseNum = courseApplicationMapper.countApplications();
        return storeNum + courseNum;
    }
}
