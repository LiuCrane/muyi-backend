package com.mysl.api.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/8/5
 */
@ApiModel("提交学员信息")
@Data
public class StudentCreateDTO implements Serializable {
    private static final long serialVersionUID = -3977630350367443042L;

    @ApiModelProperty(value = "学员姓名", required = true)
    @NotEmpty(message = "学员姓名不能为空")
    private String name;

    @ApiModelProperty("家长姓名")
    @NotEmpty(message = "家长姓名不能为空")
    @JsonProperty("parent_name")
    private String parentName;

    @ApiModelProperty("家长手机号")
    @NotEmpty(message = "家长手机号不能为空")
    @Pattern(regexp = "^(1[0-9])\\d{9}$", message = "家长手机号格式不正确")
    @JsonProperty("parent_phone")
    private String parentPhone;

    @ApiModelProperty("视力度数")
    @NotEmpty(message = "视力度数不能为空")
    private String diopter;

    @ApiModelProperty("班级id")
    @NotNull(message = "班级不能为空")
    private Long classId;

}
