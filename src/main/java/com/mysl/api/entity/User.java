package com.mysl.api.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.mysl.api.entity.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends Model<User> {

    private static final long serialVersionUID = 3907908947846019384L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private UserType type;
    @TableField(exist = false)
    private List<Role> roles;
    private Boolean active;
    @TableField(fill = FieldFill.INSERT)
    private String createdBy;
    private Date createdAt;
    @TableField(fill = FieldFill.UPDATE)
    private String updatedBy;
    @TableField(fill = FieldFill.UPDATE)
    private Date updatedAt;
}
