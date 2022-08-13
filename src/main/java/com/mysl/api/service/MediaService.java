package com.mysl.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mysl.api.entity.Media;
import com.mysl.api.entity.dto.MediaDTO;
import com.mysl.api.entity.enums.MediaType;

import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/13
 */
public interface MediaService extends IService<Media> {

    List<MediaDTO> getMediaList(Integer offset, Integer limit, Long id, MediaType type, Boolean publicly);
}
