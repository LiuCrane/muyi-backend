package com.mysl.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mysl.api.entity.Store;
import com.mysl.api.entity.dto.*;
import com.mysl.api.entity.enums.StoreStatus;

import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/10
 */
public interface StoreService extends IService<Store> {

    List<StoreFullDTO> getStores(Integer pageNum, Integer pageSize, Long id, StoreStatus status, String name,
                                 String managerName, String keyWord);

    PageInfo<StoreSimpleDTO> getFranchisees(Integer pageNum, Integer pageSize, Long excludeId);

    PageInfo<ApplicationDTO> getStores(Integer pageNum, Integer pageSize, String keyWord);

    boolean save(StoreCreateDTO dto);

    Store findByUserId(Long userId);

    boolean updateStatus(Long id, Boolean auditResult);

    boolean update(Long id, StoreUpdateDTO dto);

    boolean cancel(Long id);

}
