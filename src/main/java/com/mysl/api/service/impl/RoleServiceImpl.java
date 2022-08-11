package com.mysl.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysl.api.entity.Role;
import com.mysl.api.entity.UserRole;
import com.mysl.api.mapper.RoleMapper;
import com.mysl.api.service.RoleService;
import com.mysl.api.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ivan Su
 * @date 2022/8/11
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private UserRoleService userRoleService;

    @Override
    public List<Role> listByUserId(Long userId) {
        final List<Object> roleIds = userRoleService.listObjs(new QueryWrapper<UserRole>().eq("user_id", userId)
                .select("role_id"));

        if (CollectionUtils.isEmpty(roleIds)) {
            return null;
        }
        return new ArrayList<>(super.listByIds(roleIds.parallelStream()
                .map(Object::toString)
                .collect(Collectors.toList())));
    }
}
