package com.mysl.api.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
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

    @ApiModelProperty("学员年龄")
    @NotEmpty(message = "学员年龄不能为空")
    private Integer age;

    @ApiModelProperty("监护人姓名")
    @NotEmpty(message = "监护人姓名不能为空")
    private String guardianName;

    @ApiModelProperty("监护人手机号")
    @NotEmpty(message = "监护人手机号不能为空")
    @Pattern(regexp = "^(1[0-9])\\d{9}$", message = "监护人手机号格式不正确")
    private String guardianPhone;

    @ApiModelProperty("左眼视力")
    @NotEmpty(message = "左眼视力不能为空")
    private String leftEyeVision;

    @ApiModelProperty("右眼视力")
    @NotEmpty(message = "右眼视力不能为空")
    private String rightEyeVision;

}
