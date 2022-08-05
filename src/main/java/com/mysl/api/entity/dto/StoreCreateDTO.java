package com.mysl.api.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/8/4
 */
@ApiModel("门店提交信息")
@Data
public class StoreCreateDTO implements Serializable {

    private static final long serialVersionUID = 1802044963470442590L;

    @ApiModelProperty(value = "门店名称", required = true)
    @NotEmpty(message = "门店名称不能为空")
    private String name;

    @ApiModelProperty(value = "门店地址", required = true)
    @NotEmpty(message = "门店地址不能为空")
    private String address;

    @ApiModelProperty(value = "门店位置纬度", required = true)
    @NotEmpty(message = "纬度不能为空")
    private String lat;

    @ApiModelProperty(value = "门店位置经度", required = true)
    @NotEmpty(message = "经度不能为空")
    private String lng;

    @ApiModelProperty(value = "店长姓名", required = true)
    @NotEmpty(message = "店长姓名不能为空")
    private String managerName;

    @ApiModelProperty(value = "店长手机号码", required = true)
    @NotEmpty(message = "店长手机号码不能为空")
    @Pattern(regexp = "^(1[0-9])\\d{9}$", message = "店长手机号码格式不正确")
    private String managerPhone;

}
