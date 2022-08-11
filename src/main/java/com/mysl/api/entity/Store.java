package com.mysl.api.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.mysl.api.entity.enums.StoreStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Ivan Su
 * @date 2022/8/10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Store extends Model<Store> {

    private static final long serialVersionUID = -6883786470896259125L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String number;
    private Long managerUserId;
    private String managerIdCardNum;
    private String address;
    private String lat;
    private String lng;
    private StoreStatus status;
    private Boolean active;
    @TableField(fill = FieldFill.INSERT)
    private String createdBy;
    private Date createdAt;
    @TableField(fill = FieldFill.UPDATE)
    private String updatedBy;
    private Date updatedAt;

}
