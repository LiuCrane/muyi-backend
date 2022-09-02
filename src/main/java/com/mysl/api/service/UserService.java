package com.mysl.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mysl.api.entity.User;
import com.mysl.api.entity.dto.RegisterDTO;
import com.mysl.api.entity.dto.UserFullDTO;
import com.mysl.api.entity.dto.UserPwdUpdateDTO;
import com.mysl.api.entity.dto.UserSimpleDTO;

import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/9
 */
public interface UserService extends IService<User> {

    User findByUsername(String username);

    boolean register(RegisterDTO dto);

    boolean updatePassword(Long id, UserPwdUpdateDTO dto);

    PageInfo<UserFullDTO> getUsers(Integer pageNum, Integer pageSize, String phone, String name);

    List<UserSimpleDTO> getSimpleUsers();

}
