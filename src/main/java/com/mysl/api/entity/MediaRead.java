package com.mysl.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 媒体日志
 * </p>
 *
 * @author mac-xiang
 * @since 2021-05-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="MediaRead对象", description="媒体日志")
public class MediaRead implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "结束时间")
    private long endTime;

    @ApiModelProperty(value = "用户id")
    @TableField("`user`")
    private Long user;

    @ApiModelProperty(value = "媒体id")
    private Long media;

    @ApiModelProperty(value = "定位")
    @TableField("`location`")
    private String location;


}
