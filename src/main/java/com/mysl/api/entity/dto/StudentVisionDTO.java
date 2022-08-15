package com.mysl.api.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/8/15
 */
@ApiModel("学员视力信息")
@Data
public class StudentVisionDTO implements Serializable {
    private static final long serialVersionUID = -6151763698399667952L;

    @ApiModelProperty(value = "左眼视力", required = true)
    @JsonProperty("left_vision")
    @NotEmpty(message = "左眼视力必填")
    private String leftVision;

    @ApiModelProperty(value = "右眼视力", required = true)
    @JsonProperty("right_vision")
    @NotEmpty(message = "右眼视力必填")
    private String rightVision;

}
