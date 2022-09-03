package com.mysl.api.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/9/3
 */
@Data
@Accessors(chain = true)
public class MediaCategoryDTO implements Serializable {
    private static final long serialVersionUID = 7092970986994185774L;

    private Long id;
    private String name;
}
