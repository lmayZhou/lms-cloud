package com.lmaye.cloud.starter.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.lmaye.cloud.starter.mybatis.handler.AutoFillMetaObjectHandler;
import org.springframework.aop.interceptor.PerformanceMonitorInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * -- Mybatis Auto Config
 *
 * @author lmay.Zhou
 * @qq 379839355
 * @email lmay@lmaye.com
 * @since 2020/7/5 11:04 星期日
 */
@Configuration
@ConditionalOnClass(GlobalConfig.class)
public class MybatisAutoConfig {
    /**
     * 填充处理器
     *
     * @return MetaObjectHandler
     */
    @Bean
    @ConditionalOnClass(AutoFillMetaObjectHandler.class)
    @ConditionalOnMissingBean(MetaObjectHandler.class)
    public MetaObjectHandler metaObjectHandler() {
        return new AutoFillMetaObjectHandler();
    }

    /**
     * 配置全局属性
     *
     * @return GlobalConfig
     */
    @Bean
    @ConditionalOnMissingBean(GlobalConfig.class)
    public GlobalConfig globalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setMetaObjectHandler(metaObjectHandler());
        return globalConfig;
    }

    /**
     * Mybatis Plus 拦截器
     *
     * @return MybatisPlusInterceptor
     */
    @Bean
    @ConditionalOnMissingBean(OptimisticLockerInnerInterceptor.class)
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        // 乐观锁插件
        mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // 分页插件
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setDbType(DbType.MYSQL);
        mybatisPlusInterceptor.addInnerInterceptor(paginationInnerInterceptor);
        return mybatisPlusInterceptor;
    }

    /**
     * SQL执行效率插件
     * - 非生产环境使用
     *
     * @return PerformanceMonitorInterceptor
     */
    @Bean
    @Profile(value = {"dev", "test"})
    public PerformanceMonitorInterceptor performanceMonitorInterceptor() {
        return new PerformanceMonitorInterceptor();
    }
}
