package com.mysl.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mysl.api.entity.Store;
import com.mysl.api.entity.dto.StoreCreateDTO;
import com.mysl.api.entity.dto.StoreFullDTO;
import com.mysl.api.entity.enums.StoreStatus;

import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/10
 */
public interface StoreService extends IService<Store> {

    List<StoreFullDTO> getStores(Long id, Integer offset, Integer limit, StoreStatus status);

    boolean save(StoreCreateDTO dto);

    Store findByUserId(Long userId);

    boolean updateStatus(Long id, Boolean auditResult);

}
