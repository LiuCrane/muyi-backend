package com.mysl.api.entity.dto;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mysl.api.entity.enums.StoreStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/8/5
 */
@ApiModel("门店基础信息")
@Data
@Slf4j
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

    @ApiModelProperty(value = "最后一级地区id")
    private Long addressAreaId;

    @ApiModelProperty(value = "所在地区")
    private String addressArea;

    @ApiModelProperty(value = "所在地区级联信息")
    private AddressCascadeDTO addressAreaCascade;

    @ApiModelProperty(value = "详细地址")
    private String addressDetail;

    @JsonIgnore
    private String addressAreaCascadeJson;

    public void setAddressAreaCascadeJson(String addressAreaCascadeJson) {
        this.addressAreaCascadeJson = addressAreaCascadeJson;
        if (StringUtils.isNotEmpty(addressAreaCascadeJson)) {
            try {
                this.addressAreaCascade = JSON.parseObject(addressAreaCascadeJson, AddressCascadeDTO.class);
            } catch (Exception e) {
                log.error("JSON parseObject error: ", e);
            }
        }
    }
}
