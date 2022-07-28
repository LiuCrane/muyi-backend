package com.mysl.api.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/7/28
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO implements Serializable {

    private static final long serialVersionUID = -6301571492747373313L;

    private Long id;
    private String username;
    private String nickname;
    private String phone;
    @JsonProperty("real_name")
    private String realName;
    @JsonProperty("avatar_url")
    private String avatarUrl;

}
