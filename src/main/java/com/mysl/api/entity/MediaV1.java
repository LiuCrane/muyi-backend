package com.mysl.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mysl.api.lib.AesFile;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 媒体库
 * </p>
 *
 * @author mac-xiang
 * @since 2021-05-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Media对象", description="媒体库")
public class MediaV1 implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "修改时间")
    private long changeTime = AesFile.time();

    @ApiModelProperty(value = "类型")
    @TableField("`type`")
    private String type;

    @ApiModelProperty(value = "状态")
    @TableField("`state`")
    private Integer state;

    @ApiModelProperty(value = "排序")
    @TableField("`sort`")
    private Integer sort;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "封面图片")
    private String img;

    @ApiModelProperty(value = "简易说明")
    private String simple;

    @ApiModelProperty(value = "文件url")
    private String url;


}
