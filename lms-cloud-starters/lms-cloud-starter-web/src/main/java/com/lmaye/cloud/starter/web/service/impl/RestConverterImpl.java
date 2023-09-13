package com.lmaye.cloud.starter.web.service.impl;

import com.lmaye.cloud.starter.web.service.IRestConverter;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * -- Converter Impl
 *
 * @author lmay.Zhou
 * @date 2021/6/10 22:58
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
public class RestConverterImpl<C extends IRestConverter<T, V, D>, T extends Serializable, V extends Serializable,
        D extends Serializable> {
    /**
     * IRestConverter
     */
    @Resource
    protected C restConverter;
}
