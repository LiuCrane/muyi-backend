package com.mysl.api.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;

/**
 * @author Ivan Su
 * @date 2022/8/13
 */
@Data
public abstract class BaseModel<T extends Model<?>> extends Model<T> {
    private static final long serialVersionUID = 4520819610787074597L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Boolean active;
    @TableField(fill = FieldFill.INSERT)
    private String createdBy;
    private Date createdAt;
    @TableField(fill = FieldFill.UPDATE)
    private String updatedBy;
    @TableField(fill = FieldFill.UPDATE)
    private Date updatedAt;
}
