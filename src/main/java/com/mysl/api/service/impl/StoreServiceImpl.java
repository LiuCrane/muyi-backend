package com.mysl.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysl.api.common.exception.ResourceNotFoundException;
import com.mysl.api.entity.Store;
import com.mysl.api.entity.dto.StoreCreateDTO;
import com.mysl.api.entity.dto.StoreFullDTO;
import com.mysl.api.entity.enums.StoreStatus;
import com.mysl.api.mapper.StoreMapper;
import com.mysl.api.service.StoreService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/10
 */
@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements StoreService {

    @Override
    public List<StoreFullDTO> getStores(Long id, Integer offset, Integer limit, StoreStatus status) {
        return super.baseMapper.findAll(id, offset, limit, status);
    }

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

    @Override
    public boolean updateStatus(Long id, Boolean auditResult) {
        Store store = super.getById(id);
        if (store == null) {
            throw new ResourceNotFoundException("找不到门店信息");
        }
        if (StoreStatus.SUBMITTED.equals(store.getStatus())) {
            StoreStatus status = null;
            if (auditResult) {
                status = StoreStatus.APPROVED;
            } else {
                status = StoreStatus.REJECTED;
            }
            store.setStatus(status);
//            super.update(store, new UpdateWrapper<Store>().eq("id", id).set("status", status));
            return super.saveOrUpdate(store);
        }
        return false;
    }


}
