package com.mysl.api.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mysl.api.entity.enums.StudyProgress;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/5
 */
@ApiModel("学员信息")
@Data
public class StudentDTO implements Serializable {
    private static final long serialVersionUID = 8604484928944979080L;

    @ApiModelProperty("学员id")
    private Long id;

    @ApiModelProperty("所在班级")
    @JsonProperty("class_name")
    private String className;

    @ApiModelProperty("家长姓名")
    @JsonProperty("parent_name")
    private String parentName;

    @ApiModelProperty("家长手机号")
    @JsonProperty("parent_phone")
    private String parentPhone;

    @ApiModelProperty("近3次视力")
    private List<String> visions;

    @ApiModelProperty("视力是否提升")
    private Boolean improved;

    @ApiModelProperty("学习阶段状态")
    private StudyProgress stageStatus;

    @ApiModelProperty("当前课程")
    private String currentCourse;

    @ApiModelProperty("头像url")
    private String avatarUrl;

}
