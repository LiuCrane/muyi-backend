package com.mysl.api.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/8/1
 */
@ApiModel("登录响应")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResDTO implements Serializable {
    private static final long serialVersionUID = 646372115151214490L;
    @ApiModelProperty(value = "用户令牌")
    private String token;
}
