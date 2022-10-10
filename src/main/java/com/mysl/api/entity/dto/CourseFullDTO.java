package com.mysl.api.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mysl.api.entity.enums.ClassCourseStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/9
 */
@ApiModel("课程信息")
@Data
public class CourseFullDTO implements Serializable {

    private static final long serialVersionUID = 8615803089198599589L;

    @ApiModelProperty("课程id")
    private Long id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("简介")
    private String description;

    @ApiModelProperty("时长")
    private String duration;

    @ApiModelProperty("课程媒体")
    @JsonProperty("media_list")
    private List<MediaDTO> mediaList;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty("created_at")
    private Date createdAt;

    @ApiModelProperty(value = "修改时间")
    @JsonProperty("updated_at")
    private Date updatedAt;

    @ApiModelProperty(value = "创建人")
    @JsonProperty("created_by")
    private String createdBy;

    @ApiModelProperty(value = "修改人")
    @JsonProperty("updated_by")
    private String updatedBy;
}
