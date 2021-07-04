package com.lmaye.cloud.starter.canal.utils;

import com.lmaye.cloud.starter.canal.patterns.IType;
import com.lmaye.cloud.starter.canal.patterns.StrategyFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * -- String Convert Util
 *
 * @author Lmay Zhou
 * @date 2021/3/22 11:27
 * @email lmay@lmaye.com
 */
public class StringConvertUtil {
    static Object convertType(Class<?> type, String columnValue) {
        if (StringUtils.isBlank(columnValue)) {
            return null;
        }
        IType iType = StrategyFactory.getType(type.getTypeName());
        if(Objects.isNull(iType)) {
            return columnValue;
        }
        return iType.convertType(columnValue);
    }
}
