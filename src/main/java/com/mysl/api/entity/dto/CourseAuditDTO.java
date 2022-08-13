package com.mysl.api.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/8/13
 */
@ApiModel("课程申请审核")
@Data
public class CourseAuditDTO implements Serializable {
    private static final long serialVersionUID = -5933343766360613086L;

    @ApiModelProperty(value = "审核结果(true:通过, false:拒绝)", required = true)
    @NotNull(message = "审核结果不能为空")
    private Boolean result;
}
