package com.mysl.api.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.mysl.api.entity.enums.ClassCourseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Ivan Su
 * @date 2022/8/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassCourse extends Model<ClassCourse> {
    private static final long serialVersionUID = -5815584761062064928L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long classId;
    private Long courseId;
    private ClassCourseStatus status;

    @TableField(fill = FieldFill.INSERT)
    private String createdBy;
    private Date createdAt;
    @TableField(fill = FieldFill.UPDATE)
    private String updatedBy;
    @TableField(fill = FieldFill.UPDATE)
    private Date updatedAt;
}
