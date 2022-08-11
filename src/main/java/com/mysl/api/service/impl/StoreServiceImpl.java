package com.mysl.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysl.api.entity.Store;
import com.mysl.api.entity.dto.StoreCreateDTO;
import com.mysl.api.mapper.StoreMapper;
import com.mysl.api.service.StoreService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author Ivan Su
 * @date 2022/8/10
 */
@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements StoreService {

    @Override
    public boolean save(StoreCreateDTO dto) {
        Store store = new Store();
        BeanUtils.copyProperties(dto, store);
        return super.save(store);
    }

    @Override
    public Store findByUserId(Long userId) {
        return super.getOne(new QueryWrapper<>(Store.builder().managerUserId(userId).build()));
    }
}
