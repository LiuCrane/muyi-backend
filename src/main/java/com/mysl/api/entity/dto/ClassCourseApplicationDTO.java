package com.mysl.api.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Ivan Su
 * @date 2022/8/16
 */
@ApiModel("课程申请信息")
@Data
public class ClassCourseApplicationDTO implements Serializable {

    private static final long serialVersionUID = 4404846901589346818L;

    @ApiModelProperty("申请id")
    private Long id;

    @ApiModelProperty("门店名称")
    private String storeName;

    @ApiModelProperty("店长")
    private String storeManagerName;

    @ApiModelProperty("班级")
    private String className;

    @ApiModelProperty("课程")
    private String course;

    @ApiModelProperty("审核状态(true:同意, false:拒绝, null:待审核)")
    private Boolean result;

    @ApiModelProperty(value = "创建时间")
    private Date createdAt;

    @ApiModelProperty(value = "修改时间")
    private Date updatedAt;

    @ApiModelProperty(value = "创建人")
    private String createdBy;

    @ApiModelProperty(value = "修改人")
    private String updatedBy;
}
