package com.mysl.api.service.impl;

import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysl.api.common.GlobalConstant;
import com.mysl.api.common.exception.ResourceNotFoundException;
import com.mysl.api.entity.Store;
import com.mysl.api.entity.UserRole;
import com.mysl.api.entity.dto.StoreCreateDTO;
import com.mysl.api.entity.dto.StoreFullDTO;
import com.mysl.api.entity.dto.StoreSimpleDTO;
import com.mysl.api.entity.enums.StoreStatus;
import com.mysl.api.mapper.StoreMapper;
import com.mysl.api.service.StoreService;
import com.mysl.api.service.UserRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/10
 */
@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements StoreService {

    @Autowired
    UserRoleService userRoleService;

    @Override
    public List<StoreFullDTO> getStores(Integer pageNum, Integer pageSize, Long id, StoreStatus status) {
        PageHelper.startPage(pageNum, pageSize);
        return super.baseMapper.findAll(id, status, null);
    }

    @Override
    public PageInfo<StoreSimpleDTO> getFranchisees(Integer pageNum, Integer pageSize, Long excludeId) {
        PageHelper.startPage(pageNum, pageSize);
        List<StoreFullDTO> stores = super.baseMapper.findAll(null, StoreStatus.APPROVED, excludeId);
        PageInfo<StoreSimpleDTO> pageInfo = new PageInfo<>();
        CglibUtil.copy(new PageInfo<>(stores), pageInfo);
        pageInfo.setList(CglibUtil.copyList(stores, StoreSimpleDTO::new));
        return pageInfo;
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
    @Transactional
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
            super.saveOrUpdate(store);

            // 更新用户权限（加 ROLE_STORE_MANAGER）
            UserRole userRole = UserRole.builder().userId(store.getManagerUserId()).roleId(GlobalConstant.ROLE_STORE_MANAGER_ID).build();
            return userRoleService.save(userRole);

        }
        return false;
    }


}
