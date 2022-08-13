package com.mysl.api.entity;

import lombok.Data;

/**
 * @author Ivan Su
 * @date 2022/8/13
 */
@Data
public class Student extends BaseModel<Student> {
    private static final long serialVersionUID = -3727744756973357701L;

    private String name;
    private Long classId;
    private Long storeId;
    private String parentName;
    private String parentPhone;
    private Boolean rehabTraining;

}