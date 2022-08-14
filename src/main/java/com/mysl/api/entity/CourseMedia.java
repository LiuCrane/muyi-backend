package com.mysl.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/8/14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseMedia implements Serializable {
    private static final long serialVersionUID = 5668149424728713920L;

    private Long courseId;
    private Long mediaId;
}
