package com.mysl.api.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/9/4
 */
@ApiModel("申请审核内容")
@Data
public class AuditDTO implements Serializable {
    private static final long serialVersionUID = -5933343766360613086L;

    @ApiModelProperty(value = "申请id集合", required = true)
    @NotNull(message = "申请id不能为空")
    private List<Long> ids;
    @ApiModelProperty(value = "申请类型(STORE/STUDY)", required = true)
    @NotEmpty(message = "申请类型不能为空")
    private String type;
    @ApiModelProperty(value = "审核结果", required = true)
    @NotNull
    private Boolean result;
}
