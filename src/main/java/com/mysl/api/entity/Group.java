package com.mysl.api.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 权限组
 * </p>
 *
 * @author mac-xiang
 * @since 2021-05-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Group对象", description = "权限组")
@TableName("`group`")
public class Group implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    // @TableField(exist = false)
    // private byte[] ida;

    @ApiModelProperty(value = "权限模板")
    @TableField("`permission`")
    private byte[] permission;

    @ApiModelProperty(value = "标题")
    private String name;

    // public void setId(Long value) {
    //     if (value == null) {
    //         value = 0l;
    //     }
    //     id = value;
    //     ida = AesFile.getBytes(value);
    // }

}
