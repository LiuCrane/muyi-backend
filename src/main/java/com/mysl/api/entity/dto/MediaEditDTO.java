package com.mysl.api.entity.dto;

import com.mysl.api.entity.enums.MediaType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/8/13
 */
@ApiModel("媒体信息")
@Data
public class MediaEditDTO implements Serializable {
    private static final long serialVersionUID = 5770936545191887385L;

    @ApiModelProperty("标题")
    @NotEmpty(message = "标题不能为空")
    private String title;

    @NotNull(message = "类型不能为空")
    private MediaType type;

    @ApiModelProperty("封面图片路径")
    private String img;

    @ApiModelProperty("简介")
    private String description;

    @ApiModelProperty("媒体路径")
    @NotEmpty(message = "媒体路径不能为空")
    private String url;
}
