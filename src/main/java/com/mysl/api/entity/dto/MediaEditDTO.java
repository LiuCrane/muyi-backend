package com.mysl.api.entity.dto;

import com.mysl.api.entity.enums.MediaType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Ivan Su
 * @date 2022/8/13
 */
@ApiModel("媒体信息")
@Data
public class MediaEditDTO implements Serializable {
    private static final long serialVersionUID = 5770936545191887385L;

    @ApiModelProperty(value = "标题", required = true)
    @NotEmpty(message = "标题不能为空")
    private String title;

    @ApiModelProperty(value = "类型", required = true)
    @NotNull(message = "类型不能为空")
    private MediaType type;

    @ApiModelProperty(value = "分类", required = true)
    @NotEmpty(message = "分类不能为空")
    private String category;

    @ApiModelProperty("封面图片路径")
    private String img;

    @ApiModelProperty("简介")
    private String description;

    @ApiModelProperty(value = "媒体路径", required = true)
    @NotEmpty(message = "媒体路径不能为空")
    private String url;

    @ApiModelProperty(value = "媒体时长，单位：秒", required = true)
    @NotNull(message = "媒体时长不能为空")
    private BigDecimal duration;

    @ApiModelProperty(value = "是否公开，默认否")
    private Boolean publicly;
}
