package com.mysl.api.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author Ivan Su
 * @date 2022/8/29
 */
@Data
public class UserFullDTO extends UserDTO {
    private static final long serialVersionUID = -2408673476859345639L;

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
