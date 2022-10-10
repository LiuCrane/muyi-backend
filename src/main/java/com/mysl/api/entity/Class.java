package com.mysl.api.entity;

import com.mysl.api.entity.enums.StudyProgress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ivan Su
 * @date 2022/8/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Class extends BaseModel<Class> {
    private static final long serialVersionUID = 258280864061694685L;

    private String name;
    private String teacher;
    private Long storeId;
    private StudyProgress studyProgress;
}
