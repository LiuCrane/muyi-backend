package com.mysl.api.service.impl;

import cn.hutool.extra.cglib.CglibUtil;
import com.mysl.api.entity.OperateLog;
import com.mysl.api.mapper.OperateLogMapper;
import io.github.flyhero.easylog.model.EasyLogInfo;
import io.github.flyhero.easylog.service.ILogRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author Ivan Su
 * @date 2022/8/22
 */
@Service
@Slf4j
public class OpLogRecordServiceImpl implements ILogRecordService {

    @Autowired
    OperateLogMapper operateLogMapper;

    @Override
    @Async
    public void record(EasyLogInfo easyLogInfo) {
        log.info("opt log info: {}", easyLogInfo);
        OperateLog log = new OperateLog();
        BeanUtils.copyProperties(easyLogInfo, log);
        log.setInfo(easyLogInfo.toString());
        operateLogMapper.insert(log);
    }
}
