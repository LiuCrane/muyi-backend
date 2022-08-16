package com.mysl.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysl.api.entity.Media;
import com.mysl.api.entity.dto.MediaDTO;
import com.mysl.api.entity.enums.MediaType;
import com.mysl.api.mapper.MediaMapper;
import com.mysl.api.service.MediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/13
 */
@Service
@Slf4j
public class MediaServiceImp extends ServiceImpl<MediaMapper, Media> implements MediaService {

    @Override
    public PageInfo<MediaDTO> getMediaList(Integer pageNum, Integer pageSize, Long id, MediaType type, Boolean publicly) {
        PageHelper.startPage(pageNum, pageSize);
        List<MediaDTO> list = super.baseMapper.findAll(id, type, publicly);
        return new PageInfo<>(list);
    }
}
