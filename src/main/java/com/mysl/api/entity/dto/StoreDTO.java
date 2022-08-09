package com.mysl.api.entity.dto;

import com.mysl.api.entity.enums.StoreStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/8/5
 */
@ApiModel("门店信息")
@Data
public class StoreDTO implements Serializable {

    private static final long serialVersionUID = -2565298948612437567L;

    @ApiModelProperty("门店id")
    private Long id;

    @ApiModelProperty("门店状态")
    private StoreStatus status;

    @ApiModelProperty(value = "门店名称")
    private String name;

    @ApiModelProperty("门店编号")
    private String number;

    @ApiModelProperty(value = "门店地址")
    private String address;

    @ApiModelProperty(value = "门店位置纬度")
    private String lat;

    @ApiModelProperty(value = "门店位置经度")
    private String lng;

}
