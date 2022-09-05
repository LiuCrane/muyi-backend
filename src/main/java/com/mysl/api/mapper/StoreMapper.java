package com.mysl.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mysl.api.entity.Store;
import com.mysl.api.entity.dto.StoreFullDTO;
import com.mysl.api.entity.enums.StoreStatus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/10
 */
public interface StoreMapper extends BaseMapper<Store> {

    List<StoreFullDTO> findAll(@Param("id") Long id,
                               @Param("status") StoreStatus status,
                               @Param("exclude_id") Long excludeId,
                               @Param("name") String name,
                               @Param("manager_user_ids") List<Long> managerUserIds,
                               @Param("key_word") String keyWord);

    int countStores();

}
