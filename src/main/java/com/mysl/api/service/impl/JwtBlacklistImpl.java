package com.mysl.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysl.api.entity.JwtBlacklist;
import com.mysl.api.mapper.JwtBlacklistMapper;
import com.mysl.api.service.JwtBlacklistService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Ivan Su
 * @date 2022/8/12
 */
@Service
public class JwtBlacklistImpl extends ServiceImpl<JwtBlacklistMapper, JwtBlacklist> implements JwtBlacklistService {

    @Override
    public boolean isExist(String token) {
        return super.count(new QueryWrapper<JwtBlacklist>().eq("token", token).gt("expiration_time", new Date())) > 0;
    }
}
