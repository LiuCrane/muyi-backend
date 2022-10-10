package com.mysl.api.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Ivan Su
 * @date 2022/9/4
 */
@ApiModel("申请列表信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationDTO implements Serializable {
    private static final long serialVersionUID = -7957961785562850574L;

    @ApiModelProperty("申请id")
    private Long id;
    @ApiModelProperty("店长姓名")
    private String storeManager;
    @ApiModelProperty("门店名称")
    private String storeName;
    @ApiModelProperty("店长手机号")
    private String storeManagerPhone;
    @ApiModelProperty("地址")
    private String storeAddress;
    @ApiModelProperty("申请阶段")
    private String stage;
    @ApiModelProperty("申请时间")
    private Date createdAt;
}
