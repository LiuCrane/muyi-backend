package com.mysl.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mysl.api.entity.Student;
import com.mysl.api.entity.dto.StudentFullDTO;
import com.mysl.api.entity.dto.StudentSimpleDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/13
 */
public interface StudentMapper extends BaseMapper<Student> {

    List<StudentFullDTO> findAll(@Param("id") Long id,
                                 @Param("name") String name,
                                 @Param("store_id") Long storeId,
                                 @Param("class_id") Long classId,
                                 @Param("rehab") Boolean rehab,
                                 @Param("key_word") String keyWord);

    List<StudentSimpleDTO> findSimpleList(@Param("store_id") Long storeId, @Param("class_id") Long classId);

    int updateRehabByClassId(@Param("class_id") Long classId, @Param("updated_by") String updatedBy);
}
