package com.mysl.api.service.impl;

import cn.hutool.extra.cglib.CglibUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysl.api.common.GlobalConstant;
import com.mysl.api.common.exception.ResourceNotFoundException;
import com.mysl.api.common.exception.ServiceException;
import com.mysl.api.common.lang.ResponseData;
import com.mysl.api.config.WebSocketServer;
import com.mysl.api.entity.*;
import com.mysl.api.entity.dto.*;
import com.mysl.api.entity.enums.StoreStatus;
import com.mysl.api.entity.enums.UserType;
import com.mysl.api.mapper.AddressInfoMapper;
import com.mysl.api.mapper.StoreMapper;
import com.mysl.api.mapper.UserMapper;
import com.mysl.api.service.AddressService;
import com.mysl.api.service.ApplicationService;
import com.mysl.api.service.StoreService;
import com.mysl.api.service.UserRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Ivan Su
 * @date 2022/8/10
 */
@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements StoreService {

    @Autowired
    UserRoleService userRoleService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    ApplicationService applicationService;
    @Autowired
    AddressInfoMapper addressInfoMapper;
    @Autowired
    AddressService addressService;

    @Override
    public List<StoreFullDTO> getStores(Integer pageNum, Integer pageSize, Long id, StoreStatus status, String name, String managerName, String keyWord, Long managerUserId) {
        List<Long> managerUserIds = new ArrayList<>();
        if (StringUtils.isNotEmpty(managerName)) {
            managerUserIds = userMapper.findByNameAndType(managerName, UserType.APP_USER);
        }
        if (managerUserId != null) {
            managerUserIds.add(managerUserId);
        }
        PageHelper.startPage(pageNum, pageSize);
        return super.baseMapper.findAll(id, status, null, name, managerUserIds, keyWord);
    }

    @Override
    public PageInfo<StoreSimpleDTO> getFranchisees(Integer pageNum, Integer pageSize, Long excludeId) {
        PageHelper.startPage(pageNum, pageSize);
        List<StoreFullDTO> stores = super.baseMapper.findAll(null, StoreStatus.APPROVED, excludeId, null, null, null);
        PageInfo<StoreSimpleDTO> pageInfo = new PageInfo<>();
        CglibUtil.copy(new PageInfo<>(stores), pageInfo);
        pageInfo.setList(CglibUtil.copyList(stores, StoreSimpleDTO::new));
        return pageInfo;
    }

    @Override
    public PageInfo<ApplicationDTO> getStores(Integer pageNum, Integer pageSize, String keyWord) {
        PageHelper.startPage(pageNum, pageSize);
        List<StoreFullDTO> stores = super.baseMapper.findAll(null, StoreStatus.SUBMITTED, null, null, null, keyWord);
        PageInfo<ApplicationDTO> pageInfo = new PageInfo<>();
        CglibUtil.copy(new PageInfo<>(stores), pageInfo);
        List<ApplicationDTO> list = new ArrayList<>();
        stores.forEach(s -> list.add(ApplicationDTO.builder().id(s.getId()).createdAt(s.getCreatedAt())
                .storeName(s.getName()).storeManager(s.getManagerName())
                .storeManagerPhone(s.getManagerPhone()).storeAddress(s.getAddress()).build()));
        pageInfo.setList(list);
        return pageInfo;
    }

    @Override
    @Transactional
    public boolean save(StoreCreateDTO dto) {
        Store store = new Store();
        BeanUtils.copyProperties(dto, store);
        if (dto.getAreaId() != null) {
            Address address = addressService.getById(dto.getAreaId());
            if (address == null) {
                throw new ServiceException("所选地区不存在");
            }
            AddressCascadeDTO addressCascadeDTO = addressService.getAddressCascade(dto.getAreaId());
            AddressInfo info = AddressInfo.builder().lastAreaId(dto.getAreaId())
                    .addressCascade(JSON.toJSONString(addressCascadeDTO))
                    .addressArea(addressService.getAreaByCascade(addressCascadeDTO))
                    .addressDetail(dto.getAddressDetail()).build();
            if (addressInfoMapper.insert(info) < 1) {
                throw new ServiceException("操作失败");
            }
            store.setAddressInfoId(info.getId());
        }

        if (super.save(store)) {
            int count = applicationService.countApplications();
            WebSocketServer.send(JSON.toJSONString(ResponseData.ok(count)));
        } else {
            throw new ServiceException("操作失败");
        }
        return true;
    }

    @Override
    public Store findByUserId(Long userId) {
        return super.getOne(new QueryWrapper<>(Store.builder().managerUserId(userId).build()));
    }

    @Override
    @Transactional
    public boolean updateStatus(List<Long> ids, Boolean auditResult) {
        for (Long id : ids) {
            Store store = super.getById(id);
            if (store == null) {
                continue;
            }
            if (StoreStatus.SUBMITTED.equals(store.getStatus())) {
                StoreStatus status = null;
                if (Boolean.TRUE.equals(auditResult)) {
                    status = StoreStatus.APPROVED;
                } else {
                    status = StoreStatus.REJECTED;
                }
                store.setStatus(status);

                // 生成门店编号
                store.setNumber(String.format("%05d", store.getId()));

                super.saveOrUpdate(store);

                // 更新用户权限（加 ROLE_STORE_MANAGER）
                UserRole userRole = UserRole.builder().userId(store.getManagerUserId()).roleId(GlobalConstant.ROLE_STORE_MANAGER_ID).build();
                userRoleService.save(userRole);
            }
        }

        return true;
    }

    @Override
    @Transactional
    public boolean update(Long id, StoreUpdateDTO dto) {
        Store store = super.getById(id);
        if (store == null) {
            throw new ResourceNotFoundException("找不到门店");
        }
        if (Boolean.FALSE.equals(store.getActive())) {
            throw new ServiceException("门店已注销");
        }
        User manager = userMapper.selectById(store.getManagerUserId());
        if (!Objects.equals(dto.getManagerName(), manager.getName()) || !Objects.equals(dto.getManagerPhone(), manager.getPhone())) {
            if (!Objects.equals(dto.getManagerPhone(), manager.getPhone())) {
                int count = userMapper.countByPhone(dto.getManagerPhone());
                if (count > 0) {
                    throw new ServiceException("该手机号已被其他店长使用");
                }
            }
            userMapper.updateUserNameAndPhone(manager.getId(), dto.getManagerName(), dto.getManagerPhone());
        }
        store.setName(dto.getName());
        store.setAddress(dto.getAddress());
        return super.updateById(store);
    }

    @Override
    @Transactional
    public boolean cancel(Long id) {
        Store store = super.getById(id);
        if (store == null) {
            throw new ResourceNotFoundException("找不到门店");
        }
        if (Boolean.FALSE.equals(store.getActive())) {
            throw new ServiceException("门店已注销");
        }
        store.setActive(Boolean.FALSE);
        super.updateById(store);

        User user = userMapper.selectById(store.getManagerUserId());
        user.setActive(Boolean.FALSE);
        String dePhone = user.getPhone().replaceFirst("1", "D");
        user.setPhone(dePhone);
        user.setUsername(dePhone);
        userMapper.updateById(user);
        return true;
    }


}
