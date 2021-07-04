package com.lmaye.cloud.starter.canal.patterns;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * -- 策略工厂
 *
 * @author Lmay Zhou
 * @date 2021/4/1 18:19
 * @email lmay@lmaye.com
 */
@Component
public class StrategyFactory implements ApplicationContextAware {
    /**
     * 类型集合
     */
    private static final Map<String, IType> TYPE_MAP = new ConcurrentHashMap<>();

    public static IType getType(String value) {
        return TYPE_MAP.get(value);
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        Map<String, IType> map = context.getBeansOfType(IType.class);
        map.forEach((key, value) -> TYPE_MAP.put(value.getTypeName(), value));
    }
}
