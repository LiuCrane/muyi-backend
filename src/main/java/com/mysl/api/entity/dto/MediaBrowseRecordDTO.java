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
@ApiModel("媒体浏览记录")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaBrowseRecordDTO implements Serializable {

    private static final long serialVersionUID = 6392420328965042009L;

    private Long id;

    @ApiModelProperty("人员")
    private String userName;

    @ApiModelProperty("电话")
    private String userPhone;

    @ApiModelProperty("媒体名称")
    private String mediaTitle;

    @ApiModelProperty("媒体类型")
    private String mediaType;

    @ApiModelProperty("媒体简介")
    private String mediaDescription;

    @ApiModelProperty("开始时间")
    @JsonProperty("start_time")
    private String startTime;

    @ApiModelProperty("结束时间")
    @JsonProperty("end_time")
    private String endTime;

    @ApiModelProperty("浏览时长")
    private String totalTime;

    @ApiModelProperty("读取地点")
    private String location;

}
