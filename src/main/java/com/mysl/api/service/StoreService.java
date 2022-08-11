package com.mysl.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mysl.api.entity.Store;
import com.mysl.api.entity.dto.StoreCreateDTO;

/**
 * @author Ivan Su
 * @date 2022/8/10
 */
public interface StoreService extends IService<Store> {

    boolean save(StoreCreateDTO dto);

    Store findByUserId(Long userId);
}
