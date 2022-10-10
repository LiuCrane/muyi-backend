package com.mysl.api.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/9/14
 */
@ApiModel("课程简要信息")
@Data
public class CourseSimpleDTO implements Serializable {
    private static final long serialVersionUID = 2945307106652822033L;

    @ApiModelProperty("课程id")
    private Long id;
    @ApiModelProperty("课程标题")
    private String title;
}
