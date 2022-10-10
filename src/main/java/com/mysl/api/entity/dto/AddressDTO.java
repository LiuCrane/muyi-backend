package com.mysl.api.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/9/13
 */
@ApiModel("地区信息")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressDTO implements Serializable {
    private static final long serialVersionUID = -6244088465848262834L;

    @ApiModelProperty("地区id")
    private Long id;
    @ApiModelProperty("地区名称")
    private String name;
    @ApiModelProperty("地区子集")
    private List<AddressDTO> children;

}
