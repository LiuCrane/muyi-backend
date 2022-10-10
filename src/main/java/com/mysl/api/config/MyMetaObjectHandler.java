package com.mysl.api.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.mysl.api.config.security.JwtTokenUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createdBy", JwtTokenUtil.getCurrentUsername(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updatedBy", JwtTokenUtil.getCurrentUsername(), metaObject);
        this.setFieldValByName("updatedAt", new Date(), metaObject);
    }

}
