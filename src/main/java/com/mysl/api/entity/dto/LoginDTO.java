package com.mysl.api.entity.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/7/27
 */
@Getter
@Setter
public class LoginDTO implements Serializable {

    private static final long serialVersionUID = 8910212096398320725L;

    @NotEmpty(message = "username不能为空")
    private String username;

    @NotEmpty(message = "password不能为空")
    private String password;

}
