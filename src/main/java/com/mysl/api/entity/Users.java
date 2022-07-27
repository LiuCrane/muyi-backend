package com.mysl.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mysl.api.lib.AesFile;
import com.mysl.api.lib.Permission;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author mac-xiang
 * @since 2021-05-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Users对象", description = "用户")
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "修改时间")
    private long changeTime = AesFile.time();

    @ApiModelProperty(value = "类型")
    @TableField("`group`")
    private Long group;

    @ApiModelProperty(value = "状态")
    private Integer state;

    @ApiModelProperty(value = "拥有权限")
    @TableField("`permission`")
    private byte[] permission;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "私密信息")
    private String infoPrivate;

    @ApiModelProperty(value = "公开信息")
    private String infoPublic;

    @ApiModelProperty(value = "当前秘钥")
    @TableField("`key`")
    private byte[] key;

    @ApiModelProperty(value = "当前媒体记录")
    @TableField("`cmr`")
    private String cmr;

    @ApiModelProperty(value = "微信openid")
    @TableField("`wxid`")
    private String wxid;

    public Permission toPer() {
        return new Permission(this);
    }
}
