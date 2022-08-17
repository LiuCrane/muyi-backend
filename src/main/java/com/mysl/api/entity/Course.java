package com.mysl.api.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Ivan Su
 * @date 2022/8/14
 */
@Data
public class Course extends BaseModel<Course> {
    private static final long serialVersionUID = 8850978050817115748L;

    private String title;
    private String description;
    private BigDecimal duration;
    private String coverPath;

}
