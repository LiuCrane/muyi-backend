package com.mysl.api.entity.dto;

import io.swagger.annotations.ApiModel;
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
public class ApplicationDTO implements Serializable {
    private static final long serialVersionUID = -7957961785562850574L;

    private Long id;
    private String storeManager;
    private String storeName;
    private String storeManagerPhone;
    private String storeAddress;
    private String stage;
    private Date createdAt;
}
