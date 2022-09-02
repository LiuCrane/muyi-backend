package com.mysl.api.entity.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/9/1
 */
@ApiModel("班级更新信息")
@Data
public class ClassUpdateDTO implements Serializable {
    private static final long serialVersionUID = -9080515662449396964L;

    private String name;
}
