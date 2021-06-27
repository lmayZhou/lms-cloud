package com.lmaye.cloud.starter.mybatis.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lmaye.cloud.starter.web.service.IAppService;

import java.io.Serializable;

/**
 * -- MyBatis Service
 *
 * @author lmay.Zhou
 * @date 2020/1/2 14:10 星期四
 * @email lmay@lmaye.com
 */
public interface IMyBatisService<T, ID extends Serializable> extends IAppService<T, ID>, IService<T> {
    String PREFIX = "is_";

    /**
     * 获取别名
     *
     * @param field 字段
     * @return String
     */
    default String getAlias(String field) {
        String alias = field;
        if (alias.contains(PREFIX)) {
            alias = field.substring(PREFIX.length());
        }
        return StrUtil.toCamelCase(alias);
    }
}
