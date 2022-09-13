package com.mysl.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mysl.api.entity.Address;
import com.mysl.api.entity.dto.AddressDTO;

import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/9/13
 */
public interface AddressService extends IService<Address> {

    List<AddressDTO> getByParentId(Long parentId);

    AddressDTO getByChildId(Long childId);

    List<AddressDTO> getAll();
}
