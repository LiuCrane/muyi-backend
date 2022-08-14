package com.mysl.api.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/8/5
 */
@ApiModel("学员信息")
@Data
public class StudentSimpleDTO implements Serializable {

    private static final long serialVersionUID = 253062695188805853L;

    @ApiModelProperty("学员id")
    private Long id;

    @ApiModelProperty("学员姓名")
    private String name;

    @ApiModelProperty("学员头像")
    @JsonProperty("avatar_url")
    private String avatarUr;

}
