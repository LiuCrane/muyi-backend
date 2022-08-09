package com.mysl.api.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Ivan Su
 * @date 2022/8/5
 */
@ApiModel("学员信息")
@Data
public class StudentSimpleDTO extends StudentCreateDTO {

    private static final long serialVersionUID = 253062695188805853L;

    @ApiModelProperty("学员id")
    private Long id;

    @ApiModelProperty("学员姓名")
    private String name;

}
