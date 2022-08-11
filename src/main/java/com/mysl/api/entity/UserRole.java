package com.mysl.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ivan Su
 * @date 2022/8/11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRole {
    private Long userId;
    private Long roleId;
}
