package com.mysl.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mysl.api.entity.MediaBrowseRecord;
import com.mysl.api.entity.dto.MediaBrowseRecordDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/9/2
 */
public interface MediaBrowseRecordMapper extends BaseMapper<MediaBrowseRecord> {

    List<MediaBrowseRecordDTO> findAll(@Param("user_id") Long userId, @Param("start_time") Date startTime, @Param("end_time") Date endTime);
}
