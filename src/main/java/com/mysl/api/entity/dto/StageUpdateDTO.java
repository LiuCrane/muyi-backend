package com.mysl.api.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mysl.api.entity.enums.LearnStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/5
 */
@ApiModel("学习阶段更新信息")
@Data
public class StageUpdateDTO implements Serializable {
    private static final long serialVersionUID = -4699449431924869290L;

    @ApiModelProperty("阶段id")
    @JsonProperty("stage_id")
    private Long stageId;

    @ApiModelProperty("学员id集合")
    @JsonProperty("student_ids")
    private List<Long> studentIds;

    @ApiModelProperty("状态")
    private LearnStatus status;
}
