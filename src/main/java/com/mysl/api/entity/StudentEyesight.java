package com.mysl.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Ivan Su
 * @date 2022/8/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class StudentEyesight extends BaseModel<StudentEyesight> {
    private static final long serialVersionUID = -1860515235495834735L;

    private Long studentId;
    private String leftDiopter;
    private String rightDiopter;
    private String leftVision;
    private String rightVision;
    private Boolean improved;
    private String binocularVision;
    private Long courseId;
}
