package com.mysl.api.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
// import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan("com.mysl.api.mapper")
public class MybatisPlusConfig {
  // #region 老版本
  // @Bean
  // public PaginationInterceptor paginationInterceptor() {
  // return new PaginationInterceptor();
  // } 

  // 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor =
  // false 避免缓存出现问题

  // @Bean
  // public MybatisPlusInterceptor name() {
  // MybatisPlusInterceptor ret = new MybatisPlusInterceptor();
  // return ret;
  // }
  // #endregion
  @Bean
  public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
    return interceptor;
  }
  // #region 3.4.0 需要,3.4.1起废弃
  // @Bean
  // public ConfigurationCustomizer configurationCustomizer() {
  // // return configuration -> configuration.setUseDeprecatedExecutor(false);
  // return configuration -> configuration.setUseDeprecatedExecutor(false);
  // }
  // #endregion
}
