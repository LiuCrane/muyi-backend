package com.mysl.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mysl.api.entity.User;
import com.mysl.api.entity.dto.RegisterDTO;

/**
 * @author Ivan Su
 * @date 2022/8/9
 */
public interface UserService extends IService<User> {

    User findByUsername(String username);

    boolean register(RegisterDTO dto);

}
