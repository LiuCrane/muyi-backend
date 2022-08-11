package com.mysl.api.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author Ivan Su
 * @date 2022/8/11
 */
@Data
public class Role {
    private Long id;
    private String name;
    private String code;
    private Boolean active;
    private String createdBy;
    private Date createdAt;
    private String updatedBy;
    private Date updatedAt;
}
