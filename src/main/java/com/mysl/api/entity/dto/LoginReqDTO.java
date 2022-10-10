package com.mysl.api.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/7/27
 */
@ApiModel("登录请求")
@Data
public class LoginReqDTO implements Serializable {

    private static final long serialVersionUID = 8910212096398320725L;

    @ApiModelProperty(value = "用户名/手机号", required = true)
    @NotEmpty(message = "username不能为空")
    private String username;

    @ApiModelProperty(value = "密码", required = true)
    @NotEmpty(message = "password不能为空")
    @ToString.Exclude
    private String password;

}
