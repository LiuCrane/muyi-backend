package com.mysl.api.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/8/5
 */
@ApiModel("密码修改请求")
@Data
public class UserPwdUpdateDTO implements Serializable {
    private static final long serialVersionUID = -5842400087721497466L;

    @ApiModelProperty(value = "旧密码", required = true)
    @JsonProperty("old_password")
    @NotEmpty(message = "旧密码不能为空")
    private String oldPassword;

    @ApiModelProperty(value = "新密码", required = true)
    @JsonProperty("new_password")
    @NotEmpty(message = "新密码不能为空")
    private String newPassword;
}
