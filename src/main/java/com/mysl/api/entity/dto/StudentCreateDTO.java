package com.mysl.api.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/8/5
 */
@ApiModel("学员提交信息")
@Data
public class StudentCreateDTO implements Serializable {
    private static final long serialVersionUID = -3977630350367443042L;

    @ApiModelProperty(value = "学员姓名", required = true)
    @NotEmpty(message = "学员姓名不能为空")
    private String name;

    @ApiModelProperty(value = "学员性别(男、女)", required = true)
    @NotEmpty(message = "学员性别不能为空")
    private String gender;

    @ApiModelProperty(value = "学员年龄", required = true)
    @NotNull(message = "学员年龄不能为空")
    @Min(value = 1, message = "年龄必须大于0")
    private Integer age;

    @ApiModelProperty(value = "家长姓名", required = true)
    @NotEmpty(message = "家长姓名不能为空")
    @JsonProperty("parent_name")
    private String parentName;

    @ApiModelProperty(value = "家长手机号", required = true)
    @NotEmpty(message = "家长手机号不能为空")
    @Pattern(regexp = "^(1[0-9])\\d{9}$", message = "家长手机号格式不正确")
    @JsonProperty("parent_phone")
    private String parentPhone;

    @ApiModelProperty(value = "左眼度数")
//    @NotEmpty(message = "左眼度数不能为空")
    @JsonProperty("left_diopter")
    private String leftDiopter;

    @ApiModelProperty(value = "右眼度数")
//    @NotEmpty(message = "右眼度数不能为空")
    @JsonProperty("right_diopter")
    private String rightDiopter;

    @ApiModelProperty(value = "左眼视力", required = true)
    @NotEmpty(message = "左眼视力不能为空")
    @Digits(integer = 3, fraction = 2, message = "左眼视力格式错误")
    @JsonProperty("left_vision")
    private String leftVision;

    @ApiModelProperty(value = "右眼视力", required = true)
    @NotEmpty(message = "右眼视力不能为空")
    @Digits(integer = 3, fraction = 2, message = "右眼视力格式错误")
    @JsonProperty("right_vision")
    private String rightVision;

    @ApiModelProperty(value = "班级id", required = true)
    @NotNull(message = "班级不能为空")
    @JsonProperty("class_id")
    private Long classId;

    @ApiModelProperty(value = "双眼视力", required = true)
    @NotEmpty(message = "双眼视力不能为空")
    @Digits(integer = 3, fraction = 2, message = "双眼视力格式错误")
    private String binocularVision;

    @ApiModelProperty(value = "最后一级地区id", required = true)
    @NotNull(message = "地区id不能为空")
    private Long areaId;

    @ApiModelProperty(value = "详细地址", required = true)
    @NotEmpty(message = "详细地址不能为空")
    private String addressDetail;

}
