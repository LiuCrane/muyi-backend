package com.mysl.api.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/8/10
 */
@ApiModel("门店更新信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreUpdateDTO implements Serializable {

    private static final long serialVersionUID = -725263900152883807L;

    @ApiModelProperty("门店名称")
    @NotEmpty(message = "门店名称不能为空")
    private String name;

    @ApiModelProperty("店长名称")
    @NotEmpty(message = "店长名称不能为空")
    private String managerName;

    @ApiModelProperty("店长电话")
    @NotEmpty(message = "店长电话不能为空")
    @Pattern(regexp = "^(1[0-9])\\d{9}$", message = "手机号码格式不正确")
    private String managerPhone;

    @ApiModelProperty("门店地址")
    @NotEmpty(message = "门店地址不能为空")
    private String address;

}
