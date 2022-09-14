package com.mysl.api.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/9/14
 */
@ApiModel("所在地区级联信息")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressCascadeDTO implements Serializable {
    private static final long serialVersionUID = -7798841790312763140L;

    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("地区名称")
    private String name;
    @ApiModelProperty("下级地区")
    private AddressCascadeDTO child;
}
