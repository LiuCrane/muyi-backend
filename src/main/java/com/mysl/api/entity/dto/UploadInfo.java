package com.mysl.api.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/8/27
 */
@ApiModel("文件上传信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadInfo implements Serializable {
    private static final long serialVersionUID = 6757419244154077819L;

    @ApiModelProperty("上传URL")
    private String url;
    @ApiModelProperty("上传方式")
    private String method;
    @ApiModelProperty("上传文件名")
    private String key;
    @ApiModelProperty("存储站点")
    private String endpoint;
}
