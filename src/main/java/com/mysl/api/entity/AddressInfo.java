package com.mysl.api.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Ivan Su
 * @date 2022/9/14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressInfo extends Model<AddressInfo> {
    private static final long serialVersionUID = 3852944729610540702L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long lastAreaId;
    private String addressCascade;
    private String addressArea;
    private String addressDetail;

    @TableField(fill = FieldFill.INSERT)
    private String createdBy;
    private Date createdAt;
}
