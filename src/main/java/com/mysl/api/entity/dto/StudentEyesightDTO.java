package com.mysl.api.entity.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Ivan Su
 * @date 2022/8/15
 */
@ApiModel("学员视力数据")
@Data
public class StudentEyesightDTO implements Serializable {

    private static final long serialVersionUID = 4110200315468400294L;

    private String leftVision;
    private String rightVision;
    private Boolean improved;
    private Date createdAt;
}
