package com.mysl.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mysl.api.entity.Media;
import com.mysl.api.entity.dto.MediaDTO;
import com.mysl.api.entity.enums.MediaType;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/13
 */
public interface MediaMapper extends BaseMapper<Media> {

    List<MediaDTO> findAll(@Param("offset") Integer offset,
                           @Param("limit") Integer limit,
                           @Param("id") Long id,
                           @Param("type") MediaType type,
                           @Param("publicly") Boolean publicly);

    int sumMediaDuration(@Param("media_ids") List<Long> mediaIds);
}
