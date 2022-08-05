package com.mysl.api.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/8/5
 */
@ApiModel("学习阶段信息")
@Data
public class StageDTO implements Serializable {
    private static final long serialVersionUID = -6451679783315074497L;

    @ApiModelProperty("阶段id")
    private Long id;

    @ApiModelProperty("阶段名称")
    private String name;
}
