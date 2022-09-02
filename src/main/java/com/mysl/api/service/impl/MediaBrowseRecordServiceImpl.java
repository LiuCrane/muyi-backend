package com.mysl.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysl.api.entity.MediaBrowseRecord;
import com.mysl.api.entity.dto.MediaBrowseRecordDTO;
import com.mysl.api.mapper.MediaBrowseRecordMapper;
import com.mysl.api.service.MediaBrowseRecordService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Ivan Su
 * @date 2022/9/2
 */
@Service
public class MediaBrowseRecordServiceImpl extends ServiceImpl<MediaBrowseRecordMapper, MediaBrowseRecord> implements MediaBrowseRecordService {

    @Override
    public PageInfo<MediaBrowseRecordDTO> getRecords(Integer pageNum, Integer pageSize, Long userId, Date startTime, Date endTime) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(super.baseMapper.findAll(userId, startTime, endTime));
    }
}
