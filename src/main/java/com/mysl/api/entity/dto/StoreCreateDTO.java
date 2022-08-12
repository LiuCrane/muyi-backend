package com.mysl.api.entity.dto;

import com.mysl.api.entity.enums.StoreStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/8/10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreCreateDTO implements Serializable {
    private static final long serialVersionUID = 1802044963470442590L;

    private String name;
    private Long managerUserId;
    private String managerIdCardNum;
    private String address;
    private String lat;
    private String lng;
    private StoreStatus status;
    private String createdBy;
}
