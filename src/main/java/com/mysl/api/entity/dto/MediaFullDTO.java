package com.mysl.api.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Ivan Su
 * @date 2022/8/26
 */
@ApiModel("媒体信息")
@Data
public class MediaFullDTO extends MediaDTO {
    private static final long serialVersionUID = -7453555331423241490L;

    @ApiModelProperty(value = "媒体时长(编辑用)，单位：秒")
    private BigDecimal durationActual;

    @ApiModelProperty(value = "分类id")
    private Long categoryId;

    @ApiModelProperty(value = "修改时间")
    @JsonProperty("updated_at")
    private Date updatedAt;

    @ApiModelProperty(value = "创建人")
    @JsonProperty("created_by")
    private String createdBy;

    @ApiModelProperty(value = "修改人")
    @JsonProperty("updated_by")
    private String updatedBy;
}
