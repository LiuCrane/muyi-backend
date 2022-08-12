package com.mysl.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mysl.api.entity.Class;
import com.mysl.api.entity.dto.ClassFullDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/11
 */
public interface ClassMapper extends BaseMapper<Class>  {

    List<ClassFullDTO> findAll(@Param("offset") Integer offset,
                               @Param("limit") Integer limit,
                               @Param("id") Long id,
                               @Param("store_id") Long storeId);
}
