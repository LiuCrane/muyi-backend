package com.mysl.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mysl.api.entity.Media;
import com.mysl.api.entity.dto.MediaDTO;
import com.mysl.api.entity.dto.PlayerEventDTO;
import com.mysl.api.entity.enums.MediaType;


/**
 * @author Ivan Su
 * @date 2022/8/13
 */
public interface MediaService extends IService<Media> {

    PageInfo<MediaDTO> getMediaList(Integer pageNum, Integer pageSize, Long id, MediaType type, Boolean publicly);

    boolean savePlayerEvent(Long id, PlayerEventDTO dto, Long storeId);
}
