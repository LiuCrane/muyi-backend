package com.mysl.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysl.api.entity.UserRole;
import com.mysl.api.mapper.UserRoleMapper;
import com.mysl.api.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * @author Ivan Su
 * @date 2022/8/11
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
}
