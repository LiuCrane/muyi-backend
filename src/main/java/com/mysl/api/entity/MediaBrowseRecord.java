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
 * @date 2022/9/2
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaBrowseRecord extends Model<MediaBrowseRecord> {
    private static final long serialVersionUID = -501804800209878210L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long storeId;
    private String storeName;
    private String storeAddress;
    private String storeManager;
    private String storeManagerPhone;
    private Long classId;
    private String className;
    private String studentIds;
    private Long courseId;
    private String courseTitle;
    private Long mediaId;
    private String mediaTitle;
    private String mediaType;
    private String mediaDescription;
    private Date startTime;
    private Date endTime;
    private String totalTime;
    private Date createdAt;
    @TableField(fill = FieldFill.INSERT)
    private String createdBy;
}
