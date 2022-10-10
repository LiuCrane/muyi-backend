package com.mysl.api.service.impl;

import com.mysl.api.config.security.JwtTokenUtil;
import io.github.flyhero.easylog.service.IOperatorService;
import org.springframework.stereotype.Service;

/**
 * @author Ivan Su
 * @date 2022/8/22
 */
@Service
public class OperatorGetServiceImpl implements IOperatorService {
    @Override
    public String getOperator() {
        return JwtTokenUtil.getCurrentUsername();
    }

    @Override
    public String getTenant() {
        return null;
    }
}
