package com.mysl.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysl.api.entity.Class;
import com.mysl.api.entity.dto.ClassFullDTO;
import com.mysl.api.mapper.ClassMapper;
import com.mysl.api.service.ClassService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/13
 */
@Service
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements ClassService {
    @Override
    public List<ClassFullDTO> getClasses(Integer offset, Integer limit, Long id, Long storeId) {
        return super.baseMapper.findAll(offset, limit, id, storeId);
    }
}
