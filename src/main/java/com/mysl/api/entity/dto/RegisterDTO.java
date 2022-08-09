package com.mysl.api.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/8/3
 */
@ApiModel("注册请求")
@Data
public class RegisterDTO implements Serializable {
    private static final long serialVersionUID = 9070366283436900809L;

    @ApiModelProperty(value = "姓名", required = true)
    @NotEmpty(message = "姓名不能为空")
    private String name;

    @ApiModelProperty(value = "手机号码", required = true)
    @NotEmpty(message = "手机号码不能为空")
    @Pattern(regexp = "^(1[0-9])\\d{9}$", message = "手机号码格式不正确")
    private String phone;

    @ApiModelProperty(value = "密码", required = true)
    @NotEmpty(message = "密码不能为空")
    @Length(min = 6, message = "密码长度不能小于6位数")
    private String password;

    @ApiModelProperty(value = "身份证", required = true)
    @NotEmpty(message = "身份证不能为空")
    @JsonProperty("id_card_num")
    private String idCardNum;

    @ApiModelProperty(value = "门店名称", required = true)
    @NotEmpty(message = "门店名称不能为空")
    @JsonProperty("store_name")
    private String storeName;

    @ApiModelProperty(value = "门店地址", required = true)
    @JsonProperty("store_address")
    private String storeAddress;

    @ApiModelProperty(value = "门店位置纬度", required = true)
    @NotEmpty(message = "纬度不能为空")
    @JsonProperty("store_lat")
    private String storeLat;

    @ApiModelProperty(value = "门店位置经度", required = true)
    @NotEmpty(message = "经度不能为空")
    @JsonProperty("store_lng")
    private String storeLng;

}
