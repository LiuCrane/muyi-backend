package com.mysl.api.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mysl.api.entity.enums.ClassCourseStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/9
 */
@ApiModel("课程信息")
@Data
public class CourseCreateDTO implements Serializable {

    private static final long serialVersionUID = 2665745419840228898L;

    @ApiModelProperty("标题")
    @NotEmpty(message = "标题不能为空")
    private String title;

    @ApiModelProperty("简介")
    private String description;

    @ApiModelProperty("媒体id集合")
    @JsonProperty("media_ids")
    private List<Long> mediaIds;

}
