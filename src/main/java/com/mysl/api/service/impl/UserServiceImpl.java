package com.mysl.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysl.api.common.exception.ServiceException;
import com.mysl.api.entity.User;
import com.mysl.api.entity.dto.RegisterDTO;
import com.mysl.api.entity.dto.StoreCreateDTO;
import com.mysl.api.entity.enums.StoreStatus;
import com.mysl.api.entity.enums.UserType;
import com.mysl.api.mapper.UserMapper;
import com.mysl.api.service.StoreService;
import com.mysl.api.service.UserRoleService;
import com.mysl.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Ivan Su
 * @date 2022/8/10
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    StoreService storeService;
    @Autowired
    UserRoleService userRoleService;

    @Override
    public User findByUsername(String username) {
        log.info("findByUsername: {}", username);
        return super.getOne(new QueryWrapper<>(User.builder().username(username).build()));
    }

    @Override
    @Transactional
    public boolean register(RegisterDTO dto) {
        String password = passwordEncoder.encode(dto.getPassword());
        User user = User.builder()
                .username(dto.getPhone()).phone(dto.getPhone())
                .password(password).name(dto.getName()).type(UserType.STORE_MANAGER).build();
        if (super.save(user)) {
            // TODO 管理员审核通过后再授予店长权限
//            UserRole userRole = UserRole.builder().userId(user.getId()).roleId(GlobalConstant.ROLE_STORE_MANAGER_ID).build();
//            userRoleService.save(userRole);

            StoreCreateDTO storeCreateDTO = StoreCreateDTO.builder()
                    .name(dto.getStoreName()).address(dto.getStoreAddress())
                    .lat(dto.getStoreLat()).lng(dto.getStoreLng())
                    .managerUserId(user.getId()).managerIdCardNum(dto.getIdCardNum())
                    .status(StoreStatus.SUBMITTED).build();
            if (!storeService.save(storeCreateDTO)) {
                throw new ServiceException("注册失败");
            }
        }
        return true;
    }
}