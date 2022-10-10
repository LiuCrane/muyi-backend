package com.mysl.api.entity;

import lombok.Data;

/**
 * @author Ivan Su
 * @date 2022/8/14
 */
@Data
public class ClassCourseApplication extends BaseModel<ClassCourseApplication> {
    private static final long serialVersionUID = -3975363135820480096L;

    private Long classCourseId;
    private Boolean result;
}
