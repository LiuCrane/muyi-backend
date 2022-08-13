package com.mysl.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysl.api.entity.Media;
import com.mysl.api.entity.dto.MediaDTO;
import com.mysl.api.entity.enums.MediaType;
import com.mysl.api.mapper.MediaMapper;
import com.mysl.api.service.MediaService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/13
 */
@Service
public class MediaServiceImp extends ServiceImpl<MediaMapper, Media> implements MediaService {

    @Override
    public List<MediaDTO> getMediaList(Integer offset, Integer limit, Long id, MediaType type, Boolean publicly) {
        return super.baseMapper.findAll(offset, limit, id, type, publicly);
    }
}
