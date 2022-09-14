package com.mysl.api.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mysl.api.entity.enums.StudyProgress;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/8/9
 */
@ApiModel("班级基础信息")
@Data
public class ClassDTO implements Serializable {
    private static final long serialVersionUID = 4614811115382912322L;

    @ApiModelProperty("班级id")
    private Long id;

    @ApiModelProperty("班级名称")
    private String name;

    @ApiModelProperty("老师")
    private String teacher;

    @ApiModelProperty("门店名称")
    @JsonProperty("store_name")
    private String storeName;

    @ApiModelProperty("门店编号")
    @JsonProperty("store_number")
    private String storeNumber;

    @ApiModelProperty("学习进度(NOT_STARTED:未开始, IN_PROGRESS:进行中, REHAB_TRAINING:复健中, ENDED:已结束)")
    @JsonProperty("study_progress")
    private StudyProgress studyProgress;

    @ApiModelProperty("当前课程(阶段)")
    @JsonProperty("current_course")
    private String currentCourse;

    @ApiModelProperty("学员人数")
    @JsonProperty("student_num")
    private Integer studentNum;
}
