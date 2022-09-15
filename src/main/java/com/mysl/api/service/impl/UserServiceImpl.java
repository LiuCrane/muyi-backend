package com.mysl.api.service.impl;

import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysl.api.common.GlobalConstant;
import com.mysl.api.common.exception.ResourceNotFoundException;
import com.mysl.api.common.exception.ServiceException;
import com.mysl.api.entity.Store;
import com.mysl.api.entity.User;
import com.mysl.api.entity.UserRole;
import com.mysl.api.entity.dto.*;
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
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

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
        return super.getOne(new QueryWrapper<>(User.builder().username(username).active(Boolean.TRUE).build()));
    }

    @Override
    @Transactional
    public boolean register(RegisterDTO dto) {
        String password = passwordEncoder.encode(dto.getPassword());
        if (super.count(new QueryWrapper<User>().eq("phone", dto.getPhone())) > 0) {
            throw new ServiceException("该手机号已注册");
        }
        if (storeService.count(new QueryWrapper<Store>().eq("manager_id_card_num", dto.getIdCardNum())) > 0) {
            throw new ServiceException("该身份证号已注册");
        }
        User user = User.builder()
                .username(dto.getPhone()).phone(dto.getPhone())
                .password(password).name(dto.getName()).type(UserType.APP_USER).build();
        if (super.save(user)) {
            // 管理员未审核前只有ROLE_APP_USER
            UserRole userRole = UserRole.builder().userId(user.getId()).roleId(GlobalConstant.ROLE_APP_USER_ID).build();
            userRoleService.save(userRole);

            StoreCreateDTO storeCreateDTO = StoreCreateDTO.builder()
                    .name(dto.getStoreName()).address(dto.getStoreAddress())
                    .lat(dto.getStoreLat()).lng(dto.getStoreLng())
                    .managerUserId(user.getId()).managerIdCardNum(dto.getIdCardNum())
                    .areaId(dto.getStoreAreaId()).addressDetail(dto.getStoreAddressDetail())
                    .status(StoreStatus.SUBMITTED).createdBy(user.getUsername()).build();
            if (!storeService.save(storeCreateDTO)) {
                throw new ServiceException("注册失败");
            }
        }
        return true;
    }

    @Override
    public boolean updatePassword(Long id, UserPwdUpdateDTO dto) {
        User user = super.getById(id);
        if (user == null) {
            throw new ResourceNotFoundException("用户不存在");
        }
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new ServiceException("旧密码错误");
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        return super.updateById(user);
    }

    @Override
    public PageInfo<UserFullDTO> getUsers(Integer pageNum, Integer pageSize, String phone, String name) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(super.baseMapper.findAll(phone, name));
    }

    @Override
    public List<UserSimpleDTO> getSimpleUsers() {
        List<UserSimpleDTO> list = new ArrayList<>();
        List<User> users = super.baseMapper.selectList(new QueryWrapper<User>().eq("active", 1));
        if (!CollectionUtils.isEmpty(users)) {
            list = CglibUtil.copyList(users, UserSimpleDTO::new);
        }
        return list;
    }
}
