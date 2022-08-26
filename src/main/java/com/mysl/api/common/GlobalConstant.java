package com.mysl.api.common;

/**
 * 全局常量
 * @author Ivan Su
 * @date 2022/8/11
 */
public abstract class GlobalConstant {
    /** 后台管理员角色id */
    public static final Long ROLE_ADMIN_ID = 1L;
    /** 店长角色id */
    public static final Long ROLE_STORE_MANAGER_ID = 2L;
    public static final Long ROLE_APP_USER_ID = 3L;

    public static final String CLIENT_ADMIN = "admin";
    public static final String CLIENT_APP = "app";
}
