package com.mysl.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Ivan Su
 * @date 2022/8/12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtBlacklist extends Model<JwtBlacklist> {
    private static final long serialVersionUID = -8762118515580060531L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private String token;
    private Date expirationTime;
}
