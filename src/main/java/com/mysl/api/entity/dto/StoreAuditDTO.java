package com.mysl.api.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/8/12
 */
@ApiModel("门店审核")
@Data
public class StoreAuditDTO implements Serializable {

    private static final long serialVersionUID = 5036002257987295821L;

    @ApiModelProperty(value = "审核结果(true:通过, false:拒绝)", required = true)
    @NotNull(message = "审核结果不能为空")
    private Boolean result;
}
