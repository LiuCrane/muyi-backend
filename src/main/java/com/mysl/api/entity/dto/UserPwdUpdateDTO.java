package com.mysl.api.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/8/5
 */
@ApiModel("密码修改请求")
@Data
public class UserPwdUpdateDTO implements Serializable {
    private static final long serialVersionUID = -5842400087721497466L;

    @ApiModelProperty("旧密码")
    @JsonProperty("old_password")
    private String oldPassword;

    @ApiModelProperty("新密码")
    @JsonProperty("new_password")
    private String newPassword;
}
