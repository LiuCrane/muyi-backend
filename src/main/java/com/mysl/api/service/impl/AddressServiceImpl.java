package com.mysl.api.service.impl;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.WeakCache;
import cn.hutool.core.date.DateUnit;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysl.api.entity.Address;
import com.mysl.api.entity.dto.AddressDTO;
import com.mysl.api.mapper.AddressMapper;
import com.mysl.api.service.AddressService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/9/13
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

    @Override
    public List<AddressDTO> getByParentId(Long parentId) {
        List<Address> addresses = super.baseMapper.selectList(new QueryWrapper<Address>().eq("parent_id", parentId).orderByAsc("id"));
        List<AddressDTO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(addresses)) {
            list = CglibUtil.copyList(addresses, AddressDTO::new);
        }
        return list;
    }

    @Override
    public AddressDTO getByChildId(Long childId) {
        Address address = super.baseMapper.selectById(childId);
        if (address != null) {
            Address parent = super.baseMapper.selectById(address.getParentId());
            if (parent != null) {
                AddressDTO dto = new AddressDTO();
                CglibUtil.copy(parent, dto);
                return dto;
            }
        }
        return null;
    }

    WeakCache<String, List<AddressDTO>> addressCache = CacheUtil.newWeakCache(DateUnit.WEEK.getMillis() * 50);
    private static final String ADDRESS_CACHE_KEY = "addresses";
    @Override
    public List<AddressDTO> getAll() {
        List<AddressDTO> list = addressCache.get(ADDRESS_CACHE_KEY);
        if (CollectionUtils.isEmpty(list)) {
            list = buildTree(0L);
            addressCache.put(ADDRESS_CACHE_KEY, list);
        }
        return list;
    }

    private List<AddressDTO> buildTree(Long pid) {
        List<AddressDTO> list = new ArrayList<>();
        List<Address> addresses = super.baseMapper.selectList(new QueryWrapper<Address>().eq("parent_id", pid).orderByAsc("id"));
        if (addresses != null) {
            for (Address address : addresses) {
                AddressDTO dto = new AddressDTO();
                CglibUtil.copy(address, dto);
                dto.setChildren(buildTree(address.getId()));
                list.add(dto);
            }
        }
        return list;
    }
}
