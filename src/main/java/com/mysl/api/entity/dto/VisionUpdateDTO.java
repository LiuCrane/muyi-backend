package com.mysl.api.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/8/5
 */
@ApiModel("视力更新信息")
@Data
public class VisionUpdateDTO implements Serializable {
    private static final long serialVersionUID = -7979854363619192350L;

    @ApiModelProperty("阶段id")
    @JsonProperty("stage_id")
    private Long stageId;

    @ApiModelProperty("左眼视力")
    @JsonProperty("left_eye_vision")
    private String leftEyeVision;

    @ApiModelProperty("右眼视力")
    @JsonProperty("right_eye_vision")
    private String rightEyeVision;
}
