package com.mysl.api.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mysl.api.entity.enums.PlayerEvent;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Ivan Su
 * @date 2022/8/13
 */
@ApiModel("播放事件")
@Data
public class PlayerEventDTO {

    @ApiModelProperty("班级id")
    @JsonProperty("class_id")
    private Long classId;

    @ApiModelProperty("课程id")
    @JsonProperty("course_id")
    private Long courseId;

    @ApiModelProperty("播放事件(START:开始, PAUSE:暂停, END:结束)")
    @NotNull(message = "播放事件不能为空")
    private PlayerEvent event;
}
