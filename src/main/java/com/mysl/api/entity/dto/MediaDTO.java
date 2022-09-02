package com.mysl.api.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mysl.api.entity.enums.MediaType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/7/28
 */
@ApiModel("媒体信息")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaDTO implements Serializable {

    private static final long serialVersionUID = -6274958250092964315L;

    private Long id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("类型(AUDIO:音频, VIDEO:视频)")
    private MediaType type;

    @ApiModelProperty("封面图片路径")
    private String img;

    @ApiModelProperty("简介")
    private String description;

    @ApiModelProperty("媒体路径")
    private String url;

    @ApiModelProperty("创建时间")
    @JsonProperty("created_at")
    private String createdAt;

    @ApiModelProperty("媒体时长(前端展示用)")
    private String duration;

    @ApiModelProperty("分类")
    private String category;

}
