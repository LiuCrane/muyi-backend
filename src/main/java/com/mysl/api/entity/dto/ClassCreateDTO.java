package com.mysl.api.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/8/9
 */
@ApiModel("班级创建信息")
@Data
public class ClassCreateDTO implements Serializable {
    private static final long serialVersionUID = -8830347497325859952L;

    @ApiModelProperty(value = "班级名称", required = true)
    @NotEmpty(message = "名称不能为空")
    private String name;

    @ApiModelProperty("老师")
    private String teacher;

}
