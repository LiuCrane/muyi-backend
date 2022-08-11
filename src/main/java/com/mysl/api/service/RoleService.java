package com.mysl.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mysl.api.entity.Role;

import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/11
 */
public interface RoleService extends IService<Role> {

    List<Role> listByUserId(Long userId);
}
