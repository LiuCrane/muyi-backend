package com.mysl.api.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/7/28
 */
@ApiModel("用户信息")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO implements Serializable {

    private static final long serialVersionUID = -6301571492747373313L;

    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("手机号码")
    private String phone;

    @ApiModelProperty("真实姓名")
    @JsonProperty("real_name")
    private String realName;

    @ApiModelProperty("头像url")
    @JsonProperty("avatar_url")
    private String avatarUrl;

}
