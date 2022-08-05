package com.mysl.api.entity.dto;

import com.mysl.api.entity.enums.LearnStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Ivan Su
 * @date 2022/8/5
 */
@ApiModel("学员信息")
@Data
public class StudentDTO extends StudentCreateDTO {
    private static final long serialVersionUID = 8604484928944979080L;

    @ApiModelProperty("学员id")
    private Long id;

//    @ApiModelProperty("学习课程id")
//    private Long learnCourseId;

    @ApiModelProperty("学习课程")
    private String learnCourse;

    @ApiModelProperty("学习阶段id")
    private Long learnStageId;

    @ApiModelProperty("学习阶段")
    private String learnStage;

//    @ApiModelProperty("学习时长")
//    private String learnTime;

    @ApiModelProperty("学习阶段状态")
    private LearnStatus stageStatus;

}
