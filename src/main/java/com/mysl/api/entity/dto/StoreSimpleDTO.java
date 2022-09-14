package com.mysl.api.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mysl.api.entity.enums.StoreStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/8/5
 */
@ApiModel("门店简要信息")
@Data
public class StoreSimpleDTO implements Serializable {

    private static final long serialVersionUID = 7827903987606496439L;

    @ApiModelProperty(value = "门店名称")
    private String name;

    @ApiModelProperty("门店编号")
    private String number;

    @ApiModelProperty("店长")
    @JsonProperty("manager_name")
    private String managerName;

    @ApiModelProperty(value = "门店地址")
    private String address;

    @ApiModelProperty(value = "门店位置纬度")
    private String lat;

    @ApiModelProperty(value = "门店位置经度")
    private String lng;

}
