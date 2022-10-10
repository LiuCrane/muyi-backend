package com.mysl.api.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mysl.api.entity.enums.ClassCourseStatus;
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
    private String storeNumber;

    @ApiModelProperty("店长姓名")
    private String storeManager;

    @ApiModelProperty("学习进度(NOT_STARTED:未开始, IN_PROGRESS:进行中, REHAB_TRAINING:复健中, ENDED:已结束)")
    private StudyProgress studyProgress;

    @ApiModelProperty("当前课程(阶段)id")
    private Long currentCourseId;

    @ApiModelProperty("当前课程(阶段)")
    private String currentCourse;

    @ApiModelProperty("当前课程状态(APPLICABLE:可申请, UN_APPLICABLE:不可申请, UNDER_APPLICATION:申请中, ACCESSIBLE:可进入课程, COMPLETED:已完成, EXPIRED:已失效)")
    private ClassCourseStatus currentCourseStatus;

    @ApiModelProperty("学员人数")
    private Integer studentNum;
}
