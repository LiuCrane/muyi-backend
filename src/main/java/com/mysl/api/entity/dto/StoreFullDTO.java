package com.mysl.api.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mysl.api.entity.enums.StoreStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Ivan Su
 * @date 2022/8/5
 */
@ApiModel("门店信息")
@Data
public class StoreFullDTO extends StoreDTO {

    private static final long serialVersionUID = 2776105180749345742L;

    @ApiModelProperty(value = "店长名称")
    @JsonProperty("manager_name")
    private String managerName;

    @ApiModelProperty("店长手机号")
    @JsonProperty("manager_phone")
    private String managerPhone;

    @ApiModelProperty(value = "店长身份证号")
    @JsonProperty("manager_id_card_num")
    private String managerIdCardNum;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty("created_at")
    private Date createdAt;

    @ApiModelProperty(value = "修改时间")
    @JsonProperty("updated_at")
    private Date updatedAt;

    @ApiModelProperty(value = "创建人")
    @JsonProperty("created_by")
    private String createdBy;

    @ApiModelProperty(value = "修改人")
    @JsonProperty("updated_by")
    private String updatedBy;

}
