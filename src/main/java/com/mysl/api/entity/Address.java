package com.mysl.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * @author Ivan Su
 * @date 2022/9/12
 */
@Data
public class Address extends Model<Address>  {
    private static final long serialVersionUID = -8277745529952929524L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long parentId;

    private String name;
}
