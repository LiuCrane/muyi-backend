package com.mysl.api.common;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.WeakCache;
import cn.hutool.core.date.DateUnit;
import com.mysl.api.entity.enums.ClassCourseStatus;

/**
 * 全局常量
 *
 * @author Ivan Su
 * @date 2022/8/11
 */
public abstract class GlobalConstant {
    /**
     * 后台管理员角色id
     */
    public static final Long ROLE_ADMIN_ID = 1L;
    /**
     * 店长角色id
     */
    public static final Long ROLE_STORE_MANAGER_ID = 2L;
    public static final Long ROLE_APP_USER_ID = 3L;

    public static final String CLIENT_ADMIN = "admin";
    public static final String CLIENT_APP = "app";

    public static final String[] PERMIT_URI =
            new String[]{"/admin/auth/login", "/app/auth/login", "/app/user/register",
                    "/app/address/all", "/app/address/children", "/app/address/parent", "/cos/queue/callback"};

    public static WeakCache<String, ClassCourseStatus> courseStatusCache = CacheUtil.newWeakCache(DateUnit.HOUR.getMillis());

    public static String courseStatusCacheKeyFormat = "class_%s_course_%s_status";
}
