package com.mysl.api.service.impl;

import com.mysl.api.entity.Address;
import com.mysl.api.mapper.AddressMapper;
import com.mysl.api.service.AddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 地名 服务实现类
 * </p>
 *
 * @author mac-xiang
 * @since 2021-05-16
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

}
