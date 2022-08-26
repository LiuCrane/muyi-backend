package com.mysl.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;

/**
 * @author Ivan Su
 * @date 2022/8/25
 */
@Data
public class OperateLog extends Model<OperateLog> {
    private static final long serialVersionUID = -9064172298364631914L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private String module;
    private String type;
    private String operator;
    private Long operateTime;
    private Long executeTime;
    private Boolean success;
    private String errorMsg;
    private String result;
    private String content;
    private String info;
    private Date createdAt;
    private String bizNo;
}
