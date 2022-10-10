package com.mysl.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mysl.api.entity.User;
import com.mysl.api.entity.dto.UserFullDTO;
import com.mysl.api.entity.enums.UserType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/10
 */
public interface UserMapper extends BaseMapper<User> {

    List<UserFullDTO> findAll(@Param("phone") String phone, @Param("name") String name);

    List<Long> findByNameAndType(@Param("name") String name, @Param("type") UserType type);

    int countByPhone(@Param("phone") String phone);

    int updateUserNameAndPhone(@Param("id") Long id, @Param("name") String name, @Param("phone") String phone);
}
