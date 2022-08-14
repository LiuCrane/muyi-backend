package com.mysl.api.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author Ivan Su
 * @date 2022/8/13
 */
@ApiModel("登录请求")
@Data
public class AppLoginReqDTO extends LoginReqDTO {
    private static final long serialVersionUID = 5833340139135457677L;

    @ApiModelProperty("当前纬度")
    @NotEmpty(message = "纬度不能为空")
    private String lat;

    @ApiModelProperty("当前经度")
    @NotEmpty(message = "经度不能为空")
    private String lng;
}
