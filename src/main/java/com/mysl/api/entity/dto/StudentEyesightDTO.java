package com.mysl.api.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Ivan Su
 * @date 2022/8/15
 */
@ApiModel("学员视力数据")
@Data
public class StudentEyesightDTO implements Serializable {

    private static final long serialVersionUID = 4110200315468400294L;

    @ApiModelProperty("双眼视力")
    private String binocularVision;
    @ApiModelProperty("是否有提升")
    private Boolean improved;
    private Date createdAt;
}
