package com.mysl.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mysl.api.entity.MediaBrowseRecord;
import com.mysl.api.entity.dto.MediaBrowseRecordDTO;

import java.util.Date;

/**
 * @author Ivan Su
 * @date 2022/9/2
 */
public interface MediaBrowseRecordService extends IService<MediaBrowseRecord> {

    PageInfo<MediaBrowseRecordDTO> getRecords(Integer pageNum, Integer pageSize, Long userId, Date startTime, Date endTime);
}
