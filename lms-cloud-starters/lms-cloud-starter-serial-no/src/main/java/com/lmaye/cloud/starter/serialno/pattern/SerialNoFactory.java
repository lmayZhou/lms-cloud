package com.lmaye.cloud.starter.serialno.pattern;

import com.lmaye.cloud.starter.serialno.strategy.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * -- Serial No Factory
 *
 * @author Lmay Zhou
 * @date 2022/1/5 14:36
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Component
public class SerialNoFactory implements ApplicationContextAware {
    /**
     * 模式集合
     */
    private static final Map<String, ISerialNoStrategy> PATTERN_MAP = new ConcurrentHashMap<>();

    /**
     * 获取模式实例
     *
     * @param key 键
     * @return ISerialNoPattern
     */
    public static ISerialNoStrategy getPatternByKey(String key) {
        return PATTERN_MAP.get(key);
    }

    /**
     * 获取模式实例
     *
     * @param businessLogo 业务标识
     * @param delimiter    分隔符(默认无)
     * @param hasDate      是否带有日期(默认无)
     * @return ISerialNoStrategy
     */
    public static ISerialNoStrategy getPattern(String businessLogo, String delimiter, boolean hasDate) {
        String key = GlobalIdStrategy.class.getTypeName();
        if (StringUtils.isBlank(delimiter)) {
            if (StringUtils.isNotBlank(businessLogo) && !hasDate) {
                key = BusinessGlobalIdStrategy.class.getTypeName();
            } else if (StringUtils.isBlank(businessLogo) && hasDate) {
                key = DateGlobalIdStrategy.class.getTypeName();
            } else if (StringUtils.isNotBlank(businessLogo) && hasDate) {
                key = AllUnFormattedStrategy.class.getTypeName();
            }
        } else {
            if (StringUtils.isNotBlank(businessLogo) && hasDate) {
                key = AllFormattedStrategy.class.getTypeName();
            } else if (StringUtils.isNotBlank(businessLogo) && !hasDate) {
                key = BusinessGlobalIdFormattedStrategy.class.getTypeName();
            } else if (StringUtils.isBlank(businessLogo) && hasDate) {
                key = DateGlobalIdFormattedStrategy.class.getTypeName();
            }
        }
        return getPatternByKey(key);
    }

    /**
     * 获取模式实例
     *
     * @param businessLogo 业务标识
     * @param serialNo     业务序号
     * @param globalIdLen  全局ID长度
     * @param dateLen      日期长度
     * @return ISerialNoStrategy
     */
    public static ISerialNoStrategy getPattern(String businessLogo, String serialNo, int globalIdLen, int dateLen) {
        String key = AllFormattedStrategy.class.getTypeName();
        final int serNoLen = serialNo.length();
        if (StringUtils.isBlank(businessLogo)) {
            if (serNoLen == dateLen + globalIdLen) {
                key = DateGlobalIdStrategy.class.getTypeName();
            } else {
                key = GlobalIdStrategy.class.getTypeName();
            }
        } else {
            final int busLen = businessLogo.length();
            if (serNoLen == busLen + globalIdLen) {
                key = BusinessGlobalIdStrategy.class.getTypeName();
            } else if (serNoLen == busLen + dateLen + globalIdLen) {
                key = AllUnFormattedStrategy.class.getTypeName();
            }
        }
        return getPatternByKey(key);
    }

    /**
     * Bean 实例
     *
     * @param context ApplicationContext
     * @throws BeansException 异常
     */
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        Map<String, ISerialNoStrategy> map = context.getBeansOfType(ISerialNoStrategy.class);
        map.forEach((key, obj) -> PATTERN_MAP.put(obj.getClass().getTypeName(), obj));
    }
}
