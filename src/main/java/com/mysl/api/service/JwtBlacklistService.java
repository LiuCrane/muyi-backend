package com.mysl.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mysl.api.entity.JwtBlacklist;

/**
 * @author Ivan Su
 * @date 2022/8/12
 */
public interface JwtBlacklistService extends IService<JwtBlacklist> {

    boolean isExist(String token);

}