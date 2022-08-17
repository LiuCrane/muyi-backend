package com.mysl.api.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mysl.api.entity.enums.ClassCourseStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/8/9
 */
@ApiModel("课程信息")
@Data
public class CourseDTO implements Serializable {
    private static final long serialVersionUID = -4514856456885652533L;

    @ApiModelProperty("课程id")
    private Long id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("简介")
    private String description;

    @ApiModelProperty("时长")
    private String duration;

    @ApiModelProperty("媒体数量")
    @JsonProperty("media_num")
    private Integer mediaNum;

    @ApiModelProperty("状态(APPLICABLE:可申请, UN_APPLICABLE:不可申请, UNDER_APPLICATION:申请中, ACCESSIBLE:可进入课程, COMPLETED:已完成)")
    private ClassCourseStatus status;

    @ApiModelProperty("封面url")
    private String coverUrl;
}
