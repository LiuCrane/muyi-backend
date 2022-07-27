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
 * 用户媒体表
 * </p>
 *
 * @author mac-xiang
 * @since 2021-05-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="HaveMedia对象", description="用户媒体表")
public class HaveMedia implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "媒体id")
    private Long media;

    @ApiModelProperty(value = "剩余次数")
    @TableField("`read`")
    private Integer read;

    @ApiModelProperty(value = "用户id")
    @TableField("`user`")
    private Long user;

    @ApiModelProperty(value = "组")
    @TableField("`group`")
    private Long group;


}
