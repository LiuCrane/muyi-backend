package com.mysl.api.entity.dto;

import com.mysl.api.entity.enums.MediaType;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/8/26
 */
@Data
public class MediaSearchDTO implements Serializable {
    private static final long serialVersionUID = 4082693265450979797L;

    @ApiModelProperty("页数，默认1")
    @ApiParam(name = "page_num")
    private Integer pageNum = 1;

    @ApiModelProperty("每页记录，默认20")
    @ApiParam(name = "page_size")
    private Integer pageSize = 20;

    @ApiModelProperty("媒体标题")
    private String title;

    @ApiModelProperty("媒体类型(AUDIO:音频, VIDEO:视频)")
    private MediaType type;

    @ApiModelProperty("媒体简介")
    private String description;

    @ApiModelProperty("媒体分类")
    private String category;

    private Boolean publicly;
}
