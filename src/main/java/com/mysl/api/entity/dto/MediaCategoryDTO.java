package com.mysl.api.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/9/3
 */
@ApiModel("媒体分类")
@Data
@Accessors(chain = true)
public class MediaCategoryDTO implements Serializable {
    private static final long serialVersionUID = 7092970986994185774L;

    @ApiModelProperty("分类id")
    private Long id;
    @ApiModelProperty("分类名称")
    private String name;
}
