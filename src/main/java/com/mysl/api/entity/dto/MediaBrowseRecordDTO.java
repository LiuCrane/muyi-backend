package com.mysl.api.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/7/28
 */
@ApiModel("浏览记录")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaBrowseRecordDTO implements Serializable {

    private static final long serialVersionUID = 6392420328965042009L;

    private Long id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("简介")
    private String description;

    @ApiModelProperty("开始时间")
    @JsonProperty("start_time")
    private String startTime;

    @ApiModelProperty("结束时间")
    @JsonProperty("end_time")
    private String endTime;

    @ApiModelProperty("浏览时长")
    private String time;

    @ApiModelProperty("位置")
    private String location;

}
