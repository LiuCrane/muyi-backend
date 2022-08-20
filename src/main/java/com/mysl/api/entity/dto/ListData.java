package com.mysl.api.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/20
 */
@Data
@Accessors(chain = true)
public class ListData<T> implements Serializable {

    private static final long serialVersionUID = -6890986868250651400L;
    private List<T> list;
}
