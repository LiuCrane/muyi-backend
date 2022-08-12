package com.mysl.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mysl.api.entity.Class;
import com.mysl.api.entity.dto.ClassFullDTO;

import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/13
 */
public interface ClassService extends IService<Class> {

    List<ClassFullDTO> getClasses(Integer offset, Integer limit, Long id, Long storeId);
}
