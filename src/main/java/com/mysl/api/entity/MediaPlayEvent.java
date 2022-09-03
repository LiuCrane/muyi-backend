package com.mysl.api.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.mysl.api.entity.enums.PlayerEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Ivan Su
 * @date 2022/8/16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaPlayEvent extends Model<MediaPlayEvent> {
    private static final long serialVersionUID = -4596387101611327119L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long storeId;
    private Long classCourseId;
    private Long mediaId;
    private PlayerEvent playerEvent;
    private Date createdAt;
    @TableField(fill = FieldFill.INSERT)
    private String createdBy;
    private Boolean recorded;
    private Long userId;

}
