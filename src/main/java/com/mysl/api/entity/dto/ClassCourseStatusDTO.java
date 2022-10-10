package com.mysl.api.entity.dto;

import com.mysl.api.entity.enums.ClassCourseStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/9/14
 */
@ApiModel("班级课程状态")
@Data
@Accessors(chain = true)
public class ClassCourseStatusDTO implements Serializable {
    private static final long serialVersionUID = 8558139104841959565L;
    @ApiModelProperty("状态(APPLICABLE:可申请, UN_APPLICABLE:不可申请, UNDER_APPLICATION:申请中, ACCESSIBLE:可进入课程, COMPLETED:已完成, EXPIRED:已失效)")
    private ClassCourseStatus status;
}
