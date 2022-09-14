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
    private String avatarUrl;
    private String leftVision;
    private String rightVision;
    private String leftDiopter;
    private String rightDiopter;
    private Boolean improved;
    private String gender;
    private Integer age;
    private Long addressInfoId;
    private String binocularVision;

}
