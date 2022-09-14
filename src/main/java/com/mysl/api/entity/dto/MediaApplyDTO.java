package com.mysl.api.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/5
 */
@ApiModel("媒体申请信息")
@Data
public class MediaApplyDTO implements Serializable {
    private static final long serialVersionUID = -2745458956758533367L;

    @ApiModelProperty("学员id集合")
    @JsonProperty("student_ids")
    private List<Long> studentIds;

}
