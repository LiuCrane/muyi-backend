package com.mysl.api.service.impl;

import com.mysl.api.entity.Media;
import com.mysl.api.mapper.MediaMapper;
import com.mysl.api.service.MediaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 媒体库 服务实现类
 * </p>
 *
 * @author mac-xiang
 * @since 2021-05-16
 */
@Service
public class MediaServiceImpl extends ServiceImpl<MediaMapper, Media> implements MediaService {

}
