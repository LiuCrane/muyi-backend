package com.mysl.api.entity.dto;

import com.mysl.api.entity.enums.StoreStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Ivan Su
 * @date 2022/8/5
 */
@ApiModel("门店信息")
@Data
public class StoreDTO extends StoreCreateDTO {

    private static final long serialVersionUID = -2565298948612437567L;

    @ApiModelProperty("门店id")
    private Long id;

    @ApiModelProperty("门店状态")
    private StoreStatus status;

}
