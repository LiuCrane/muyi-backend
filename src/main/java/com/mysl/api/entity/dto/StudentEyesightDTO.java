package com.mysl.api.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Ivan Su
 * @date 2022/8/15
 */
@ApiModel("学员视力数据")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentEyesightDTO implements Serializable {

    private static final long serialVersionUID = 4110200315468400294L;

    @ApiModelProperty("课程(阶段)标题")
    private String title;
    @ApiModelProperty("左眼视力")
    private String leftVision;
    @ApiModelProperty("右眼视力")
    private String rightVision;
    @ApiModelProperty("双眼视力")
    private String binocularVision;
    @ApiModelProperty("是否有提升")
    private Boolean improved;
    private Date createdAt;
    private Date updatedAt;
}
