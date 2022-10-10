package com.mysl.api.entity.dto;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/5
 */
@ApiModel("学员基础信息")
@Data
@Slf4j
public class StudentDTO implements Serializable {
    private static final long serialVersionUID = 8604484928944979080L;

    @ApiModelProperty("学员id")
    private Long id;

    @ApiModelProperty("学员姓名")
    private String name;

    @ApiModelProperty("学员性别")
    private String gender;

    @ApiModelProperty("学员年龄")
    private Integer age;

    @ApiModelProperty("所在班级")
    @JsonProperty("class_name")
    private String className;

    @ApiModelProperty("家长姓名")
    @JsonProperty("parent_name")
    private String parentName;

    @ApiModelProperty("家长手机号")
    @JsonProperty("parent_phone")
    private String parentPhone;

    @ApiModelProperty("视力是否有提升(true:有提升，false:无提升，null:持平)")
    private Boolean improved;

    @ApiModelProperty("当前课程")
    private String currentCourse;

    @ApiModelProperty("头像url")
    private String avatarUrl;

    @ApiModelProperty("视力数据列表")
    private List<StudentEyesightDTO> eyesightList;

    @ApiModelProperty(value = "疗愈前左眼视力")
    private String firstLeftVision;

    @ApiModelProperty(value = "疗愈前右眼视力")
    private String firstRightVision;

    @ApiModelProperty(value = "疗愈前双眼视力")
    private String firstBinocularVision;

    @ApiModelProperty("最新双眼视力")
    private String binocularVision;

    @ApiModelProperty(value = "最后一级地区id")
    private Long addressAreaId;

    @ApiModelProperty(value = "所在地区")
    private String addressArea;

    @ApiModelProperty(value = "所在地区级联信息")
    private AddressCascadeDTO addressAreaCascade;

    @ApiModelProperty(value = "详细地址")
    private String addressDetail;

    @JsonIgnore
    private String addressAreaCascadeJson;

    public void setAddressAreaCascadeJson(String addressAreaCascadeJson) {
        this.addressAreaCascadeJson = addressAreaCascadeJson;
        if (StringUtils.isNotEmpty(addressAreaCascadeJson)) {
            try {
                this.addressAreaCascade = JSON.parseObject(addressAreaCascadeJson, AddressCascadeDTO.class);
            } catch (Exception e) {
                log.error("JSON parseObject error: ", e);
            }
        }
    }

    @ApiModelProperty(value = "地址")
    private String address;

}
